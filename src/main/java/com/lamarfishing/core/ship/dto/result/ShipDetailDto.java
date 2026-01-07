package com.lamarfishing.core.ship.dto.result;

import lombok.Builder;
import lombok.Getter;

/**
 * 출항 일정 상세 조회에 사용되는 dto
 */
@Getter
@Builder
public class ShipDetailDto {

    private Long shipId;
    private String fishType;
    private Integer price;
    private Integer maxHeadCount;
    private String notification;

    public ShipDetailDto(Long shipId, String fishType, Integer price, Integer maxHeadCount, String notification) {
        this.shipId = shipId;
        this.fishType = fishType;
        this.price = price;
        this.maxHeadCount = maxHeadCount;
        this.notification = notification;
    }
}
