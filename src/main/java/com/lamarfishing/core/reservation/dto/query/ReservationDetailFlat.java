package com.lamarfishing.core.reservation.dto.query;

import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
public class ReservationDetailFlat {
    //ReservationDetailShipDto
    private final String fishType;
    private final String notification;

    //ReservationDetailDto
    private final String username;
    private final String nickname;
    private final String phone;
    private final Integer headCount;
    private final Long couponId;
    private final String request;
    private final Integer totalPrice;
    private final Reservation.Process process;

    //ReservationDetailScheduleDto
    private final LocalDateTime departure;
    private final Integer tide;

    //UserDto

    public ReservationDetailFlat(String fishType, String notification, String username,
                                 String nickname, String phone, Integer headCount, Long couponId,
                                 String request, Integer totalPrice, Reservation.Process process,
                                 LocalDateTime departure, Integer tide) {
        this.fishType = fishType;
        this.notification = notification;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.headCount = headCount;
        this.couponId = couponId;
        this.request = request;
        this.totalPrice = totalPrice;
        this.process = process;
        this.departure = departure;
        this.tide = tide;
    }
}
