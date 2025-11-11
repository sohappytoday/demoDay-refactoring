package com.lamarfishing.core.ship.dto.response;

import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShipListResponse {
    List<ShipDetailDto> ships;

    public static ShipListResponse from(List<ShipDetailDto> ships){
        return ShipListResponse.builder()
                .ships(ships)
                .build();
    }
}
