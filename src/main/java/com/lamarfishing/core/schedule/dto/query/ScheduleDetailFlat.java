package com.lamarfishing.core.schedule.dto.query;

import com.lamarfishing.core.schedule.domain.Type;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDetailFlat {

    //ShipDetailDto
    private Long shipId;
    private String fishType;
    private Integer price;
    private Integer maxHeadCount;
    private String notification;

    //ScheduleDetailDto
    private String schedulePublicId;
    private LocalDateTime departure;
    private Integer tide;
    private Integer remainHeadCount;
    private Type type;

    //List<ReservationCommonDto>
    //별도


    public ScheduleDetailFlat(Long shipId, String fishType, Integer price, Integer maxHeadCount, String notification, String schedulePublicId, LocalDateTime departure, Integer tide, Integer remainHeadCount, Type type) {
        this.shipId = shipId;
        this.fishType = fishType;
        this.price = price;
        this.maxHeadCount = maxHeadCount;
        this.notification = notification;
        this.schedulePublicId = schedulePublicId;
        this.departure = departure;
        this.tide = tide;
        this.remainHeadCount = remainHeadCount;
        this.type = type;
    }
}
