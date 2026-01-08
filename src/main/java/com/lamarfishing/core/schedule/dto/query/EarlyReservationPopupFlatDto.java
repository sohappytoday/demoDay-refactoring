package com.lamarfishing.core.schedule.dto.query;

import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.user.domain.Grade;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EarlyReservationPopupFlatDto {
    //ReservationShipDto
    private String fishType;
    private Integer price;
    private String notification;

    private String schedulePublicId;
    private Type scheduleType;
    private Integer remainHeadCount;
    private LocalDateTime departure;
    private Integer tide;

    public EarlyReservationPopupFlatDto(String fishType, Integer price, String notification, String schedulePublicId, Type scheduleType, Integer remainHeadCount, LocalDateTime departure, Integer tide) {
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
