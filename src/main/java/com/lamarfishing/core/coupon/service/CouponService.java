package com.lamarfishing.core.coupon.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void issueCoupon(Long userId, String publicId) {
        if (!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if (user.getGrade() != User.Grade.ADMIN) {
            throw new InvalidUserGrade();
        }

        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        User receiver = reservation.getUser();
        LocalDateTime departure = reservation.getSchedule().getDeparture();
        //주말인지 아닌지
        boolean isWeekend = departure.getDayOfWeek() == DayOfWeek.SATURDAY
                || departure.getDayOfWeek() == DayOfWeek.SUNDAY;

        if (isWeekend) {
            //주말이면 주말쿠폰 생성
            Coupon coupon = Coupon.create(Coupon.Type.WEEKEND, receiver);
            couponRepository.save(coupon);
            return;
        }
        Coupon coupon = Coupon.create(Coupon.Type.WEEKDAY, receiver);
        couponRepository.save(coupon);
    }
}
