package com.lamarfishing.core.schedule.service.command;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.exception.CouponNotFound;
import com.lamarfishing.core.coupon.exception.UnauthorizedCouponAccess;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.service.command.ReservationCommandService;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ReservationPopupCommand;
import com.lamarfishing.core.schedule.dto.result.ReservationCreateResult;
import com.lamarfishing.core.schedule.exception.InvalidHeadCount;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationPopupCommandService {

    private final ScheduleRepository scheduleRepository;
    private final CouponRepository couponRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCommandService reservationCommandService;
    private final StatisticService statisticService;
    private final UserRepository userRepository;
    /**
     * 회원 예약
     */
    @Transactional
    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public ReservationCreateResult createReservationUser(
            User user, String publicId, ReservationPopupCommand command) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        int headCount = command.getHeadCount();
        if(headCount + schedule.getCurrentHeadCount() > schedule.getShip().getMaxHeadCount()){
            throw new InvalidHeadCount();
        }
        int totalPrice = ship.getPrice() * headCount;

        Coupon coupon = null;
        if (command.getCouponId() != null) {
            coupon = couponRepository.findById(command.getCouponId())
                    .orElseThrow(CouponNotFound::new);

            if (!coupon.getUser().equals(user)) {
                throw new UnauthorizedCouponAccess();
            }
            coupon.use();
        }

        schedule.increaseCurrentHeadCount(headCount);
        Reservation reservation = Reservation.create(headCount,command.getRequest(),totalPrice, Reservation.Process.RESERVE_COMPLETED,user,schedule,coupon);
        reservationRepository.save(reservation);

        reservationCommandService.sendReservationReceiptNotification(user, schedule, ship, totalPrice, headCount);

        statisticService.afterReservation(LocalDate.now(), publicId, headCount);

        return ReservationMapper.toReservationCreateResult(reservation);
    }

    /**
     * 비회원 예약
     */
    @Transactional
    public ReservationCreateResult createReservationGuest(String publicId, ReservationPopupCommand command) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        User user = User.createAnonymous(command.getUsername(), command.getNickname(), command.getPhone());
        userRepository.save(user);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        int headCount = command.getHeadCount();

        if(headCount + schedule.getCurrentHeadCount() > schedule.getShip().getMaxHeadCount()){
            throw new InvalidHeadCount();
        }
        int totalPrice = ship.getPrice() * headCount;


        Coupon coupon = null;

        schedule.increaseCurrentHeadCount(headCount);
        Reservation reservation = Reservation.create(headCount,command.getRequest(),totalPrice, Reservation.Process.RESERVE_COMPLETED,user,schedule,coupon);
        reservationRepository.save(reservation);

        reservationCommandService.sendReservationReceiptNotification(user, schedule, ship, totalPrice, headCount);

        statisticService.afterReservation(LocalDate.now(), publicId, headCount);

        return ReservationMapper.toReservationCreateResult(reservation);
    }
}
