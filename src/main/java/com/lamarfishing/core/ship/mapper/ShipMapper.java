package com.lamarfishing.core.ship.mapper;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;

public class ShipMapper {

    public static ShipDetailDto toShipDetailDto(Ship ship){
        return ShipDetailDto.builder()
                .shipId(ship.getId())
                .fishType(ship.getFishType())
                .price(ship.getPrice())
                .build();
    }

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
