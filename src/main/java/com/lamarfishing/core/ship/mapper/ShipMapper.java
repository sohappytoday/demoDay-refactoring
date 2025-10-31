package com.lamarfishing.core.ship.mapper;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipCommonDto;

public class ShipMapper {

    public static ShipCommonDto toShipCommonResponse(Ship ship){
        return ShipCommonDto.builder()
                .shipId(ship.getId())
                .fishType(ship.getFishType())
                .price(ship.getPrice())
                .build();
    }
}
