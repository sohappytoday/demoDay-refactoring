package com.lamarfishing.core.schedule.dto.response;


import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import com.lamarfishing.core.ship.dto.command.ShipCommonDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleDetailResponse {

    private ShipCommonDto ship;
    private ScheduleDetailDto schedule;
    private List<ReservationCommonDto> reservations;

    public static ScheduleDetailResponse from(ShipCommonDto shipCommonDto, ScheduleDetailDto scheduleDetailDto, List<ReservationCommonDto> reservations) {
        return ScheduleDetailResponse.builder()
                .ship(shipCommonDto)
                .schedule(scheduleDetailDto)
                .reservations(reservations)
                .build();
    }

}
