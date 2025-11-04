package com.lamarfishing.core.ship.dto.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationShipDto {
    private Long shipId;
    private String fishType;
    private Integer price;
    private Integer remainHeadCount;
    private String notification;
}
