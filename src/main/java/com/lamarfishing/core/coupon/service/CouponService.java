package com.lamarfishing.core.coupon.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.validate.ValidatePublicId;
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

        ValidatePublicId.validateReservationPublicId(publicId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Reservation reservation = reservationRepository.findByPublicId(publicId)
                .orElseThrow(ReservationNotFound::new);

        User receiver = reservation.getUser();
        LocalDateTime departure = reservation.getSchedule().getDeparture();

        Coupon.Type type;

        if (isWeekend(departure)) {
            type = Coupon.Type.WEEKEND;
        } else {
            type = Coupon.Type.WEEKDAY;
        }

        Coupon coupon = Coupon.create(type, receiver);
        couponRepository.save(coupon);
    }

    private boolean isWeekend(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private User findUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        return user;
    }

    private Reservation findReservation(String publicId) {
        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        return reservation;
    }

}
