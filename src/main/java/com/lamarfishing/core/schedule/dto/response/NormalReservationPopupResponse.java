package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.dto.result.NormalReservationPopupResult;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
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

    public static NormalReservationPopupResponse from(NormalReservationPopupResult result) {
        return NormalReservationPopupResponse.builder()
                .ship(result.getShip())
                .user(result.getUser())
                .schedulePublicId(result.getSchedulePublicId())
                .remainHeadCount(result.getRemainHeadCount())
                .departure(result.getDeparture())
                .dayOfWeek(result.getDeparture().getDayOfWeek())
                .tide(result.getTide())
                .build();
    }
}