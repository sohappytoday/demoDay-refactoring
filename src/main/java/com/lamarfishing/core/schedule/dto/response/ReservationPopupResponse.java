package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;
import com.lamarfishing.core.user.dto.command.ReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationPopupResponse {
    private ReservationShipDto ship;
    private ReservationUserDto user;
    private String schedulePublicId;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static ReservationPopupResponse from(Schedule schedule,Integer remainHeadCount, ReservationUserDto user, ReservationShipDto ship) {
        return ReservationPopupResponse.builder()
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
