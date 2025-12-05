package com.lamarfishing.core.schedule.dto.response;


import com.lamarfishing.core.reservation.dto.common.ReservationCommonDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleDetailResponse {

    private ShipDetailDto ship;
    private ScheduleDetailDto schedule;
    private List<ReservationCommonDto> reservations;

    public static ScheduleDetailResponse from(ShipDetailDto shipDetailDto, ScheduleDetailDto scheduleDetailDto, List<ReservationCommonDto> reservations) {
        return ScheduleDetailResponse.builder()
                .ship(shipDetailDto)
                .schedule(scheduleDetailDto)
                .reservations(reservations)
                .build();
    }

}
