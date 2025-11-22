package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.message.service.MessageService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.request.ReservationProcessUpdateRequest;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.exception.InvalidRequestContent;
import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ReservationDetailScheduleDto;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
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
@Transactional(readOnly = true)
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final MessageService messageService;

    public ReservationDetailResponse getReservationDetail(Long userId, String publicId) {
        if (!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        //관리자나 예약자가 아니면 거부
        if (!user.getGrade().equals(User.Grade.ADMIN) && !reservation.getUser().equals(user)) {
            throw new InvalidUserGrade();
        }

        Schedule schedule = reservation.getSchedule();
        Ship ship = schedule.getShip();

        ReservationDetailShipDto reservationDetailShipDto = ShipMapper.toReservationDetailShipDto(ship);
        ReservationDetailDto reservationDetailDto = ReservationMapper.toReservationDetailDto(reservation);
        ReservationDetailScheduleDto reservationDetailScheduleDto = ScheduleMapper.toReservationDetailScheduleDto(schedule);

        ReservationDetailResponse response = ReservationDetailResponse.from(reservationDetailShipDto, reservationDetailDto, reservationDetailScheduleDto);

        return response;
    }

    @Transactional
    public void changeReservationProcess(Long userId, String publicId, ReservationProcessUpdateRequest request) {
        if (!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }

        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Reservation.Process requestProcess = request.getProcess();
        if (requestProcess == null) {
            throw new InvalidRequestContent();
        }
        // User Logic
        if (user.getGrade() != User.Grade.ADMIN) {
            if (requestProcess != Reservation.Process.CANCEL_REQUESTED) {
                throw new InvalidRequestContent();
            }
            reservation.changeProcess(requestProcess);
            return;
        }

        // Admin Logic
        if(requestProcess == Reservation.Process.CANCEL_REQUESTED) {
            throw new InvalidRequestContent();
        }

        if (requestProcess == Reservation.Process.CANCEL_COMPLETED) {
            reservation.changeProcess(requestProcess);

            Schedule schedule = reservation.getSchedule();
            schedule.decreaseCurrentHeadCount(reservation.getHeadCount());
            return;
        }

        // 예약 완료, 입금 완료
        reservation.changeProcess(requestProcess);
    }

    /*************************************************************************/
    //자동 메시지 관련 메서드

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

        messageService.sendMessage(phones,msg);
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

        messageService.sendMessage(phones,msg);
    }

    public void sendReservationReceiptNotification(User user,Schedule schedule,Ship ship,int totalPrice, int headCount){

        LocalDate msgScheduleDate = schedule.getDeparture().toLocalDate();
        String msgScheduleDay = getKoreanDay(msgScheduleDate);

        List<Integer> phone = new ArrayList<>();
        String msg = "안녕하세요 쭈불 낚시입니다. \n" +
                msgScheduleDate + msgScheduleDay +  // 2025-11-28(토)
                " " + ship.getFishType()+ " 예약 접수 확인 안내 문자 드립니다. 입금 완료 후에 예약이 확정되니, 아래의 계좌로 금액만큼 입금해주시길 바랍니다.\n" +
                "[예약정보]\n" +
                "출항일: "+ msgScheduleDate + msgScheduleDay + " " + schedule.getTide() +"물 " + ship.getFishType() +"\n" +
                "예약자명: " + user.getUsername() + "\n" +
                "닉네임: " + user.getNickname() + "\n" +
                "예약인원: " + headCount + "명\n" +
                "입금계좌: 신한 110-345-678910\n" +
                "입금금액: " + totalPrice + "원";
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
