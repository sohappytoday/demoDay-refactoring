package com.lamarfishing.core.schedule.dto.response;


import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.result.ScheduleDetailResult;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleDetailResponse {

    private ShipDetailDto ship;
    private ScheduleDetailDto schedule;
    private List<ReservationCommonDto> reservations;

    public static ScheduleDetailResponse from(ScheduleDetailResult result){
        return ScheduleDetailResponse.builder()
                .ship(result.getShip())
                .schedule(result.getSchedule())
                .reservations(result.getReservations())
                .build();
    }

}
