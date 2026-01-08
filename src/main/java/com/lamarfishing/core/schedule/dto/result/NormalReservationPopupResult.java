package com.lamarfishing.core.schedule.dto.result;

import com.lamarfishing.core.schedule.dto.query.ReservationPopupFlatDto;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import lombok.Builder;
import lombok.Data;

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

    public static NormalReservationPopupResult of(ReservationPopupFlatDto flatDto,
                                                  ReservationShipDto ship,
                                                  NormalReservationUserDto user) {
        return NormalReservationPopupResult.builder()
                .ship(ship)
                .user(user)
                .schedulePublicId(flatDto.getSchedulePublicId())
                .remainHeadCount(flatDto.getRemainHeadCount())
                .departure(flatDto.getDeparture())
                .tide(flatDto.getTide())
                .build();
    }
}