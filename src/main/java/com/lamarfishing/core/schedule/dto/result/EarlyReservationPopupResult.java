package com.lamarfishing.core.schedule.dto.result;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.query.EarlyReservationPopupFlatDto;
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
    private Integer tide;

    public static EarlyReservationPopupResult of(EarlyReservationPopupFlatDto flatDto,
                                                 ReservationShipDto shipDto,
                                                 EarlyReservationUserDto userDto){
        return EarlyReservationPopupResult.builder()
                .ship(shipDto)
                .user(userDto)
                .schedulePublicId(flatDto.getSchedulePublicId())
                .remainHeadCount(flatDto.getRemainHeadCount())
                .departure(flatDto.getDeparture())
                .tide(flatDto.getTide())
                .build();
    }
}