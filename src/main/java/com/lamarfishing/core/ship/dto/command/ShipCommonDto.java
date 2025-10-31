package com.lamarfishing.core.ship.dto.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShipCommonDto {

    private Long shipId;
    private String fishType;
    private Integer price;


}
