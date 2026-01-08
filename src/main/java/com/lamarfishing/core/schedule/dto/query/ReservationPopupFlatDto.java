package com.lamarfishing.core.schedule.dto.query;

import com.lamarfishing.core.schedule.domain.Type;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationPopupFlatDto {
    //ReservationShipDto
    private String fishType;
    private Integer price;
    private String notification;

    private String schedulePublicId;
    private Type scheduleType;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private Integer tide;

    public ReservationPopupFlatDto(String fishType, Integer price, String notification, String schedulePublicId, Type scheduleType, Integer remainHeadCount, LocalDateTime departure, Integer tide) {
        this.fishType = fishType;
        this.price = price;
        this.notification = notification;
        this.schedulePublicId = schedulePublicId;
        this.scheduleType = scheduleType;
        this.remainHeadCount = remainHeadCount;
        this.departure = departure;
        this.tide = tide;
    }
}
