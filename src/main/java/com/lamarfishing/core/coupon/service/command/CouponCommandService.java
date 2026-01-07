package com.lamarfishing.core.coupon.service.command;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCommandService {

    private final ReservationRepository reservationRepository;
    private final CouponRepository couponRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void issueCoupon(String publicId) {

        ValidatePublicId.validateReservationPublicId(publicId);

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
}
