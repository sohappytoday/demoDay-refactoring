package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.exception.CouponNotFound;
import com.lamarfishing.core.coupon.exception.UnauthorizedCouponAccess;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.log.statistic.service.StatisticService;
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
import com.lamarfishing.core.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * parameter userId만 받게 fix 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationPopupService {

    private final StatisticService statisticService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    /**
     * 선예약 팝업 조회
     */
    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public EarlyReservationPopupResponse getEarlyReservationPopup(User user, String publicId) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
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
     * 일반예약 팝업 조회 (회원, 관리자)
     */
    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public NormalReservationPopupResponse getNormalReservationPopupUser(User user, String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.NORMAL){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto(user);
        return NormalReservationPopupResponse.from(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);

    }

    /**
     * 비회원 예약 팝업 조회
     */
   public NormalReservationPopupResponse getNormalReservationPopupGuest(String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.NORMAL){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto();

        return NormalReservationPopupResponse.from(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);

    }

    /**
     * 회원 예약
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public ReservationCreateResponse createReservationUser(
            User user, String publicId,
            String username, String nickname, String phone, int headCount, String userRequest, Long couponId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        if(headCount + schedule.getCurrentHeadCount() > schedule.getShip().getMaxHeadCount()){
            throw new InvalidHeadCount();
        }
        int totalPrice = ship.getPrice() * headCount;

        Coupon coupon = null;
        if (couponId != null) {
            coupon = couponRepository.findById(couponId)
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

        statisticService.afterReservation(LocalDate.now(), publicId, headCount);

        ReservationCreateResponse reservationCreateResponse = ReservationMapper.toReservationCreateResponse(reservation);

        return reservationCreateResponse;
    }

    /**
     * 비회원 예약
     */
    @Transactional
    public ReservationCreateResponse createReservationGuest(String publicId, String username, String nickname,
                                                            String phone, int headCount, String userRequest, Long couponId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        User user = User.createAnonymous(username, nickname, phone);
        userRepository.save(user);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        if(headCount + schedule.getCurrentHeadCount() > schedule.getShip().getMaxHeadCount()){
            throw new InvalidHeadCount();
        }
        int totalPrice = ship.getPrice() * headCount;


        Coupon coupon = null;

        schedule.increaseCurrentHeadCount(headCount);
        Reservation reservation = Reservation.create(headCount,userRequest,totalPrice, Reservation.Process.RESERVE_COMPLETED,user,schedule,coupon);
        reservationRepository.save(reservation);

        reservationService.sendReservationReceiptNotification(user, schedule, ship, totalPrice, headCount);

        statisticService.afterReservation(LocalDate.now(), publicId, headCount);

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
