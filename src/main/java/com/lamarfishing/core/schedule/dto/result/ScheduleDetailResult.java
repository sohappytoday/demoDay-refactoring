package com.lamarfishing.core.schedule.dto.result;

import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleDetailResult {

    private ShipDetailDto ship;
    private ScheduleDetailDto schedule;
    private List<ReservationCommonDto> reservations;

    public static ScheduleDetailResult of(ShipDetailDto shipDetailDto, ScheduleDetailDto scheduleDetailDto, List<ReservationCommonDto> reservations) {
        return ScheduleDetailResult.builder()
                .ship(shipDetailDto)
                .schedule(scheduleDetailDto)
                .reservations(reservations)
                .build();
    }

}