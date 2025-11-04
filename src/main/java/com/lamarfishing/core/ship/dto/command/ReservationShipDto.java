package com.lamarfishing.core.ship.dto.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationShipDto {
    private String fishType;
    private Integer price;
    private String notification;
}
