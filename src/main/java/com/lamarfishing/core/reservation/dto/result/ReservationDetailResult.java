package com.lamarfishing.core.reservation.dto.result;

import com.lamarfishing.core.reservation.dto.command.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.schedule.dto.command.ReservationDetailScheduleDto;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDetailResult {
    private ReservationDetailShipDto ship;
    private ReservationDetailDto reservation;
    private ReservationDetailScheduleDto schedule;

    public static ReservationDetailResult from(ReservationDetailShipDto ship, ReservationDetailDto reservation, ReservationDetailScheduleDto schedule) {
        return ReservationDetailResult.builder()
                .ship(ship)
                .reservation(reservation)
                .schedule(schedule)
                .build();
    }
}