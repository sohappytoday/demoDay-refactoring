package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.exception.CouponNotFound;
import com.lamarfishing.core.coupon.exception.UnauthorizedCouponAccess;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.service.ReservationService;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.NormalReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.exception.InvalidHeadCount;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.exception.UnauthorizedPopupAccess;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.EarlyReservationUserDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.mapper.UserMapper;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

/**
 * parameter userId만 받게 fix 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationPopupService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    /**
     * 선예약 팝업 조회
     */
    public EarlyReservationPopupResponse getEarlyReservationPopup(Long userId, String publicId) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if (user.getGrade() == Grade.GUEST){
            throw new UnauthorizedPopupAccess();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.EARLY){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        List<CouponCommonDto> coupons = couponRepository.findByUserAndStatusAndType(user, Coupon.Status.AVAILABLE,getCouponTypeByDeparture(schedule.getDeparture()))
                .stream()
                .map(CouponMapper::toCouponCommonDto)
                .toList();
        EarlyReservationUserDto earlyReservationUserDto = UserMapper.toEarlyReservationUserDto(user,coupons);
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        return EarlyReservationPopupResponse.from(schedule,remainHeadCount, earlyReservationUserDto,reservationShipDto);
    }

    /**
     * 일반예약 팝업 조회
     */
    public NormalReservationPopupResponse getNormalReservationPopup(Long userId, String publicId) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.NORMAL){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        if (user.getGrade() == Grade.GUEST){
            NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto();
            return NormalReservationPopupResponse.from(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);
        }

        NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto(user);
        return NormalReservationPopupResponse.from(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);


    }

    @Transactional
    public ReservationCreateResponse createReservation(Long userId, String publicId, ReservationPopupRequest reservationPopupRequest) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Grade userGrade = user.getGrade();

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();
        int headCount = reservationPopupRequest.getHeadCount();
        if(headCount + schedule.getCurrentHeadCount() > schedule.getShip().getMaxHeadCount()){
            throw new InvalidHeadCount();
        }
        int totalPrice = ship.getPrice() * headCount;
        String userRequest = reservationPopupRequest.getRequest();

        //비회원이라면
        if (userGrade == Grade.GUEST) {
            String username = reservationPopupRequest.getUsername();
            String nickname = reservationPopupRequest.getNickname();
            String phone = reservationPopupRequest.getPhone();
            //게스트 업데이트
            user.updateGuestInfo(username, nickname, phone);
        }

        Coupon coupon = null;
        if (reservationPopupRequest.getCouponId() != null) {
            coupon = couponRepository.findById(reservationPopupRequest.getCouponId())
                    .orElseThrow(CouponNotFound::new);

            if (!coupon.getUser().equals(user)) {
                throw new UnauthorizedCouponAccess();
            }
            coupon.use();
        }

        schedule.increaseCurrentHeadCount(headCount);
        Reservation reservation = Reservation.create(headCount,userRequest,totalPrice, Reservation.Process.RESERVE_COMPLETED,user,schedule,coupon);
        reservationRepository.save(reservation);

        reservationService.sendReservationReceiptNotification(user, schedule, ship, totalPrice, headCount);

        ReservationCreateResponse reservationCreateResponse = ReservationMapper.toReservationCreateResponse(reservation);

        return reservationCreateResponse;
    }

    private Coupon.Type getCouponTypeByDeparture(LocalDateTime departure) {
        DayOfWeek day = departure.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return Coupon.Type.WEEKEND;
        }
        return Coupon.Type.WEEKDAY;
    }
}
