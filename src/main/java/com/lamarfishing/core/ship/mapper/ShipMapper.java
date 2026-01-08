package com.lamarfishing.core.ship.mapper;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.result.ReservationDetailShipDto;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;

public class ShipMapper {


    public static ReservationShipDto toReservationShipDto(Ship ship){
        return ReservationShipDto.builder()
                .fishType(ship.getFishType())
                .price(ship.getPrice())
                .notification(ship.getNotification())
                .build();
    }

    public static ReservationDetailShipDto toReservationDetailShipDto(Ship ship){
        return ReservationDetailShipDto.builder()
                .fishType(ship.getFishType())
                .notification(ship.getNotification())
                .build();
    }
}
