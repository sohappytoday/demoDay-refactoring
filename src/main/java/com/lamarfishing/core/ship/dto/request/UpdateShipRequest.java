package com.lamarfishing.core.ship.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateShipRequest {
    private String fishType;
    private Integer price;
    private Integer maxHeadCount;
    private String notification;
}
