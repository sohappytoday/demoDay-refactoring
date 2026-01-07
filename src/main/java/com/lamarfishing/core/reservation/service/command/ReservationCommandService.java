package com.lamarfishing.core.reservation.service.command;

import com.lamarfishing.core.log.statistic.domain.Statistic;
import com.lamarfishing.core.log.statistic.repository.StatisticRepository;
import com.lamarfishing.core.message.service.command.MessageCommandService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationProcessUpdateCommand;
import com.lamarfishing.core.reservation.exception.InvalidRequestContent;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import com.lamarfishing.core.reservation.domain.Reservation.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final MessageCommandService messageCommandService;
    private final StatisticRepository statisticRepository;



    /**
     * 일반 유저가 예약
     */
    // @PreAuthorize("hasAnyAuthority('GRADE_BASIC', 'GRADE_VIP')")
    public void reservationCancelRequest(String publicId, ReservationProcessUpdateCommand command) {

        ValidatePublicId.validateReservationPublicId(publicId);

        Reservation reservation = findReservation(publicId);
        Process process = command.getProcess();

        if (process != Reservation.Process.CANCEL_REQUESTED) {
            throw new InvalidRequestContent();
        }

        reservation.changeProcess(process);
    }

    /**
     * Admin이 예약 상태 변경
     */
    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void changeReservationProcess(String publicId, ReservationProcessUpdateCommand command) {

        ValidatePublicId.validateReservationPublicId(publicId);

        Reservation reservation = findReservation(publicId);
        Process process = command.getProcess();

        if(process == Reservation.Process.CANCEL_REQUESTED) {
            throw new InvalidRequestContent();
        }

        if (process == Reservation.Process.CANCEL_COMPLETED) {
            reservation.changeProcess(process);

            Schedule schedule = reservation.getSchedule();
            schedule.decreaseCurrentHeadCount(reservation.getHeadCount());
            sendReservationCanceledNotification(reservation);
            return;
        }

        // 예약 완료
        if(process == Process.RESERVE_COMPLETED) {
            reservation.changeProcess(process);
            return;
        }
        // 입금 완료
        reservation.changeProcess(process);
    }
    /**
     * private Method
     */

    private Reservation findReservation(String pubilcId){
        Reservation reservation = reservationRepository.findByPublicId(pubilcId).orElseThrow(ReservationNotFound::new);
        return reservation;
    }


    /*************************************************************************
    /**************************자동 메시지 관련 메서드****************************
    /*************************************************************************

    /**
     * 입금 만료 마감 통계 분석
     */

    @Transactional
    public void statisticPaymentDeadlineWarning(){
        LocalDate today = LocalDate.now();  //11월 22일
        LocalDate deadline = today.plusDays(4); //11월 26일

        LocalDateTime scheduleStart = deadline.atStartOfDay();  //11월 26일 오전 0시
        LocalDateTime scheduleEnd = deadline.atTime(23,59,59);  //11월 26일 23시 59분 59초

        Schedule schedule = scheduleRepository.findFirstByDepartureBetween(scheduleStart,scheduleEnd)
                .orElse(null);

        if (schedule == null) {
            return;
        }

        List<Reservation> reservations = reservationRepository
                .findByScheduleAndProcess(schedule, Reservation.Process.RESERVE_COMPLETED);

        Statistic statistic = statisticRepository.findByDate(today);
        for (Reservation reservation : reservations) {
            statistic.addDeposit24Hour();
        }

    }

    @Transactional
    public void statisticPaymentExpiredNotification(){
        LocalDate today = LocalDate.now();  //11월 22일
        LocalDate deadline = today.plusDays(3); //11월 25일

        LocalDateTime scheduleStart = deadline.atStartOfDay();  //11월 25일 오전 0시
        LocalDateTime scheduleEnd = deadline.atTime(23,59,59);

        Schedule schedule = scheduleRepository.findFirstByDepartureBetween(scheduleStart,scheduleEnd)
                .orElse(null);

        if (schedule == null) {
            return;
        }

        List<Reservation> reservations = reservationRepository
                .findByScheduleAndProcess(schedule, Reservation.Process.RESERVE_COMPLETED);

        Statistic statistic = statisticRepository.findByDate(today);
        for (Reservation reservation : reservations) {
            statistic.addDepositExpired();
        }
    }

    /**
     * 입금 만료 마감 경고
     */
    public void sendPaymentDeadlineWarning(){
        LocalDate today = LocalDate.now();  //11월 22일
        LocalDate deadline = today.plusDays(4); //11월 26일

        LocalDateTime scheduleStart = deadline.atStartOfDay();  //11월 26일 오전 0시
        LocalDateTime scheduleEnd = deadline.atTime(23,59,59);  //11월 26일 23시 59분 59초

        Schedule schedule = scheduleRepository.findFirstByDepartureBetween(scheduleStart,scheduleEnd)
                .orElse(null);

        if (schedule == null) {
            return;
        }

        List<Reservation> reservations = reservationRepository
                .findByScheduleAndProcess(schedule, Reservation.Process.RESERVE_COMPLETED);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            phones.add(reservation.getUser().getPhone());
        }

        String msg = createPaymentWarningMessage(schedule);

        messageCommandService.sendMessage(phones,msg);
    }

    /**
     * 입금 만료
     */
    @Transactional
    public void sendPaymentExpiredNotification(){
        LocalDate today = LocalDate.now();  //11월 22일
        LocalDate deadline = today.plusDays(3); //11월 25일

        LocalDateTime scheduleStart = deadline.atStartOfDay();  //11월 25일 오전 0시
        LocalDateTime scheduleEnd = deadline.atTime(23,59,59);

        Schedule schedule = scheduleRepository.findFirstByDepartureBetween(scheduleStart,scheduleEnd)
                .orElse(null);

        if (schedule == null) {
            return;
        }

        List<Reservation> reservations = reservationRepository
                .findByScheduleAndProcess(schedule, Reservation.Process.RESERVE_COMPLETED);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            phones.add(reservation.getUser().getPhone());
            expireReservation(reservation);         //reservation 취소
        }

        String msg = createPaymentExpiredMessage(schedule);

        messageCommandService.sendMessage(phones,msg);
    }

    public void sendReservationReceiptNotification(User user,Schedule schedule,Ship ship,int totalPrice, int headCount){

        LocalDate msgScheduleDate = schedule.getDeparture().toLocalDate();
        String msgScheduleDay = getKoreanDay(msgScheduleDate);

        List<String> phones = new ArrayList<>();
        phones.add(user.getPhone());

        String strTotalPrice = formatWithComma(totalPrice);
        String msg = "안녕하세요 쭈불 낚시입니다. \n" +
                msgScheduleDate + msgScheduleDay +  // 2025-11-28(토)
                " " + ship.getFishType()+ " 예약 접수 확인 안내 문자 드립니다. 입금 완료 후에 예약이 확정되니, 아래의 계좌로 금액만큼 입금해주시길 바랍니다.\n" +
                "[예약정보]\n" +
                "출항일: "+ msgScheduleDate + msgScheduleDay + " " + schedule.getTide() +"물 " + ship.getFishType() +"\n" +
                "예약자명: " + user.getUsername() + "\n" +
                "닉네임: " + user.getNickname() + "\n" +
                "예약인원: " + headCount + "명\n" +
                "입금계좌: 신한 110-345-678910\n" +
                "입금금액: " + strTotalPrice + "원\n" +
                "문의사항은 010-6264-7243으로 연락주세요";

        messageCommandService.sendMessage(phones,msg);
    }

    public void sendReservationCanceledNotification(Reservation reservation){
        Schedule schedule =  reservation.getSchedule();
        User user = reservation.getUser();
        Ship ship = schedule.getShip();
        LocalDate msgScheduleDate = schedule.getDeparture().toLocalDate();
        String msgScheduleDay = getKoreanDay(msgScheduleDate);

        List<String> phones = new ArrayList<>();
        phones.add(user.getPhone());
        String strTotalPrice = formatWithComma(reservation.getTotalPrice());

        String msg = "안녕하세요. 신청하신 예약 취소 접수가 완료되었습니다.\n" +
                "출항일: " + msgScheduleDate + msgScheduleDay +" " + schedule.getTide() +"물 " + ship.getFishType() +
                "\n" +
                "취소자명: " + user.getUsername() + "\n" +
                "닉네임: " + user.getNickname() + "\n" +
                "취소인원: " + reservation.getHeadCount() + "\n" +
                "환불금액: " + strTotalPrice + "원";

        messageCommandService.sendMessage(phones,msg);
    }

    private String formatWithComma(int number) {
        return String.format("%,d", number);
    }

    private void expireReservation(Reservation reservation) {
        reservation.changeProcess(Reservation.Process.CANCEL_COMPLETED);

        Schedule schedule = reservation.getSchedule();
        schedule.decreaseCurrentHeadCount(reservation.getHeadCount());
    }

    private String createPaymentExpiredMessage(Schedule schedule) {
        LocalDate msgScheduleDate = schedule.getDeparture().toLocalDate();
        String msgScheduleDay = getKoreanDay(msgScheduleDate);

        return "안녕하세요 쭈불 낚시입니다.\n" +
                msgScheduleDate + " " + msgScheduleDay + " " +
                "쭈갑 예약 입금기한이 만료되어 예약이 자동 취소되었습니다.\n";
    }

    private String createPaymentWarningMessage(Schedule schedule) {
        LocalDate msgScheduleDate = schedule.getDeparture().toLocalDate();
        String msgScheduleDay = getKoreanDay(msgScheduleDate);

        return "안녕하세요 쭈불 낚시입니다.\n" +
                msgScheduleDate + " " + msgScheduleDay + " " +
                "쭈갑 예약 입금 재안내 문자 드립니다.\n" +
                "입금 마감시간 이후에는 예약이 자동 취소되니," +
                "입금 마감시간 전까지 아래 계좌로 금액만큼 입금해주시길 바랍니다.\n" +
                "멋사은행 11-1111-11111-11111 예금주: 쭈불";
    }

    private String getKoreanDay(LocalDate date){
        String shortDay = date.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        String day = "(" + shortDay + ")";

        return day;
    }
}
