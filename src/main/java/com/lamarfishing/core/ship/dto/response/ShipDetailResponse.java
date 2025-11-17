package com.lamarfishing.core.ship.dto.response;

import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShipDetailResponse {
    ShipDetailDto ship;

    public static ShipDetailResponse from(ShipDetailDto ship){
        return ShipDetailResponse.builder()
                .ship(ship)
                .build();
    }
}
