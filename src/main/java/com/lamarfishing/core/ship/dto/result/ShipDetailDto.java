package com.lamarfishing.core.ship.dto.result;

import com.lamarfishing.core.schedule.dto.query.ScheduleDetailFlat;
import com.lamarfishing.core.ship.domain.Ship;
import lombok.Builder;
import lombok.Getter;

/**
 * 출항 일정 상세 조회에 사용되는 dto
 */
@Getter
public class ShipDetailDto {

    private Long shipId;
    private String fishType;
    private Integer price;
    private Integer maxHeadCount;
    private String notification;

    private ShipDetailDto(Long shipId, String fishType, Integer price, Integer maxHeadCount, String notification) {
        this.shipId = shipId;
        this.fishType = fishType;
        this.price = price;
        this.maxHeadCount = maxHeadCount;
        this.notification = notification;
    }

    public static ShipDetailDto from(ScheduleDetailFlat dto){
        return new ShipDetailDto(dto.getShipId(),  dto.getFishType(), dto.getPrice(), dto.getMaxHeadCount(), dto.getNotification());
    }
}
