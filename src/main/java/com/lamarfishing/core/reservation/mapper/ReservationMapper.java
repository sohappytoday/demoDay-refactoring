package com.lamarfishing.core.reservation.mapper;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;

public class ReservationMapper {

    public static ReservationCommonDto toReservationCommonDto(Reservation reservation) {
        return ReservationCommonDto.builder()
                .reservationPublicId(reservation.getPublicId())
                .headCount(reservation.getHeadCount())
                .process(reservation.getProcess())
                .build();
    }
}
