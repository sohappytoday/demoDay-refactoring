package com.lamarfishing.core.schedule.dummy;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleDummy {

    private final ScheduleRepository scheduleRepository;
    private final ShipRepository shipRepository;

    public void init() {

        if (scheduleRepository.count() > 0) {
            return;
        }

        List<Ship> ships = shipRepository.findAll();

        LocalDateTime baseDate = LocalDateTime.of(2025, 11, 10, 5, 0);

        for (int i = 0; i < 30; i++) {
            // ship 순환 (1,2,3,4,5 반복)
            Ship ship = ships.get(i % 5);

            // tide 1부터 12까지 순환
            int tide = (i % 12) + 1;

            // 첫 일정만 EARLY, 나머지는 NORMAL
            Type type = (i == 0) ? Type.EARLY : Type.NORMAL;

            Status status = Status.WAITING;

            // 날짜 하루씩 증가
            LocalDateTime departure = baseDate.plusDays(i);

            // currentHeadCount = ship maxHeadCount
            int currentHeadCount = ship.getMaxHeadCount();

            Schedule schedule = Schedule.create(departure, currentHeadCount, tide, status, type, ship);

            scheduleRepository.save(schedule);
        }
    }
}
