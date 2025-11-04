package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.dto.command.ReservationUserDto;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationPopupResponse {
    private ShipDetailDto ship;
    private ReservationUserDto user;
    private String schedulePublicId;
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static ReservationPopupResponse from(Schedule schedule, ReservationUserDto user,ShipDetailDto ship) {
        return ReservationPopupResponse.builder()
                .ship(ship)
                .schedulePublicId(schedule.getPublicId())
                .user(user)
                .departure(schedule.getDeparture())
                .dayOfWeek(schedule.getDeparture().getDayOfWeek())
                .tide(schedule.getTide())
                .build();
    }
}
