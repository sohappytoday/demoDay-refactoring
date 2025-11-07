package com.lamarfishing.core.reservation.dto.response;

import com.lamarfishing.core.reservation.dto.command.ReservationDetailDto;
import com.lamarfishing.core.schedule.dto.command.ReservationDetailScheduleDto;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDetailResponse {
    private ReservationDetailShipDto ship;
    private ReservationDetailDto reservation;
    private ReservationDetailScheduleDto schedule;

    public static ReservationDetailResponse from(ReservationDetailShipDto ship, ReservationDetailDto reservation, ReservationDetailScheduleDto schedule) {
        return ReservationDetailResponse.builder()
                .ship(ship)
                .reservation(reservation)
                .schedule(schedule)
                .build();
    }
}
