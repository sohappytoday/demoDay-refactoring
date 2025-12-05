package com.lamarfishing.core.reservation.dto.response;

import com.lamarfishing.core.reservation.dto.common.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.result.ReservationDetailResult;
import com.lamarfishing.core.schedule.dto.common.ReservationDetailScheduleDto;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationDetailResponse {
    private ReservationDetailShipDto ship;
    private ReservationDetailDto reservation;
    private ReservationDetailScheduleDto schedule;

    public static ReservationDetailResponse from(ReservationDetailResult result) {
        return ReservationDetailResponse.builder()
                .ship(result.getShip())
                .reservation(result.getReservation())
                .schedule(result.getSchedule())
                .build();
    }

}
