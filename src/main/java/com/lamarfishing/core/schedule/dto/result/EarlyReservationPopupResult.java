package com.lamarfishing.core.schedule.dto.result;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.user.dto.query.EarlyReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@Builder
public class EarlyReservationPopupResult {
    private ReservationShipDto ship;
    private EarlyReservationUserDto user;
    private String schedulePublicId;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static EarlyReservationPopupResult of(Schedule schedule, Integer remainHeadCount, EarlyReservationUserDto user, ReservationShipDto ship) {
        return EarlyReservationPopupResult.builder()
                .ship(ship)
                .user(user)
                .schedulePublicId(schedule.getPublicId())
                .remainHeadCount(remainHeadCount)
                .departure(schedule.getDeparture())
                .dayOfWeek(schedule.getDeparture().getDayOfWeek())
                .tide(schedule.getTide())
                .build();
    }
}