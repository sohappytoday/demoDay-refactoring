package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.dto.result.EarlyReservationPopupResult;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.user.dto.query.EarlyReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 선예약 팝업 조회
 */
@Data
@Builder
public class EarlyReservationPopupResponse {
    private ReservationShipDto ship;
    private EarlyReservationUserDto user;
    private String schedulePublicId;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static EarlyReservationPopupResponse from(EarlyReservationPopupResult result) {
        return EarlyReservationPopupResponse.builder()
                .ship(result.getShip())
                .user(result.getUser())
                .schedulePublicId(result.getSchedulePublicId())
                .remainHeadCount(result.getRemainHeadCount())
                .departure(result.getDeparture())
                .dayOfWeek(result.getDayOfWeek())
                .tide(result.getTide())
                .build();
    }
}
