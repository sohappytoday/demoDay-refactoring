package com.lamarfishing.core.reservation.dummy;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.lamarfishing.core.user.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationDummy {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CouponRepository couponRepository;

    private final Random random = new Random();

    public void init() {

        if (reservationRepository.count() > 0) {
            return;
        }

        // 1. BASIC/VIP/GUEST 유저만 가져오기
        List<User> users = userRepository.findAll().stream()
                .filter(u -> u.getGrade() == User.Grade.BASIC
                        || u.getGrade() == User.Grade.VIP
                        || u.getGrade() == User.Grade.GUEST)
                .collect(Collectors.toList());

        // 2. 모든 쿠폰 가져와서 user별 AVAILABLE 쿠폰 리스트 매핑
        Map<Long, List<Coupon>> availableCouponsByUser = couponRepository.findAll().stream()
                .filter(c -> c.getStatus() == Coupon.Status.AVAILABLE)
                .collect(Collectors.groupingBy(c -> c.getUser().getId()));

        // 3. 스케줄(30개) 가져오기
        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {

            List<User> shuffledUsers = new ArrayList<>(users);
            Collections.shuffle(shuffledUsers);

            List<User> selectedUsers = shuffledUsers.subList(0, Math.min(5, shuffledUsers.size()));

            for (User user : selectedUsers) {

                int headCount = random.nextInt(3) + 1;

                int price = schedule.getShip().getPrice() * headCount;

                // 유저의 AVAILABLE 쿠폰 중 랜덤 선택 (없으면 null)
                List<Coupon> userCoupons = availableCouponsByUser.getOrDefault(
                        user.getId(),
                        Collections.emptyList()
                );
                Coupon coupon = userCoupons.isEmpty()
                        ? null
                        : userCoupons.get(random.nextInt(userCoupons.size()));

                Reservation reservation = Reservation.create(
                        headCount,
                        "예약자가 보낸 메시지입니다",
                        price,
                        Reservation.Process.RESERVE_COMPLETED,
                        user,
                        schedule,
                        coupon
                );
                schedule.increaseCurrentHeadCount(headCount);
                reservationRepository.save(reservation);
            }
        }

    }
}
