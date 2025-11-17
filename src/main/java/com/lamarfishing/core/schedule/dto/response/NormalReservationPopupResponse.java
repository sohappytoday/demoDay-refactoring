package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;
import com.lamarfishing.core.user.dto.command.EarlyReservationUserDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 일반예약 팝업 조회
 */
@Data
@Builder
public class NormalReservationPopupResponse {
    private ReservationShipDto ship;
    private NormalReservationUserDto user;
    private String schedulePublicId;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static NormalReservationPopupResponse from(Schedule schedule, Integer remainHeadCount, NormalReservationUserDto user, ReservationShipDto ship) {
        return NormalReservationPopupResponse.builder()
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