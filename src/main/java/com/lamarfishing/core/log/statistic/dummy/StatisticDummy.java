package com.lamarfishing.core.log.statistic.dummy;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.log.statistic.domain.Statistic;
import com.lamarfishing.core.log.statistic.repository.StatisticRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatisticDummy {

    private final StatisticRepository statisticRepository;

    public void init() {

        if (statisticRepository.count() > 0) {
            return;
        }

        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 2, 2);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Statistic dummy = Statistic.create(0,0,0,
                    0,0,0,date);
            statisticRepository.save(dummy);
        }

    }
}

