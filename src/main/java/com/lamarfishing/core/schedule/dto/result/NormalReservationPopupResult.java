package com.lamarfishing.core.schedule.dto.result;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@Builder
public class NormalReservationPopupResult {
    private ReservationShipDto ship;
    private NormalReservationUserDto user;
    private String schedulePublicId;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private Integer tide;

    public static NormalReservationPopupResult of(Schedule schedule, Integer remainHeadCount, NormalReservationUserDto user, ReservationShipDto ship) {
        return NormalReservationPopupResult.builder()
                .ship(ship)
                .user(user)
                .schedulePublicId(schedule.getPublicId())
                .remainHeadCount(remainHeadCount)
                .departure(schedule.getDeparture())
                .tide(schedule.getTide())
                .build();
    }
}