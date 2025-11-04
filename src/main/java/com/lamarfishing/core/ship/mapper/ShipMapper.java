package com.lamarfishing.core.ship.mapper;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipCommonDto;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;

public class ShipMapper {

    public static ShipCommonDto toShipCommonResponse(Ship ship){
        return ShipCommonDto.builder()
                .shipId(ship.getId())
                .fishType(ship.getFishType())
                .price(ship.getPrice())
                .build();
    }

    public static ReservationShipDto toReservationShipResponse(Ship ship){
        return ReservationShipDto.builder()
                .fishType(ship.getFishType())
                .price(ship.getPrice())
                .notification(ship.getNotification())
                .build();
    }
}
