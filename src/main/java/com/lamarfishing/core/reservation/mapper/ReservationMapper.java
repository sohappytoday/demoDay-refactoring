package com.lamarfishing.core.reservation.mapper;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.reservation.dto.command.ReservationDetailDto;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;

public class ReservationMapper {

    public static ReservationCommonDto toReservationCommonDto(Reservation reservation) {
        Grade grade = reservation.getUser().getGrade();
        String nickname = reservation.getUser().getNickname();

        return ReservationCommonDto.builder()
                .reservationPublicId(reservation.getPublicId())
                .nickname(nickname)
                .grade(grade)
                .headCount(reservation.getHeadCount())
                .process(reservation.getProcess())
                .build();
    }

    public static ReservationCreateResponse toReservationCreateResponse(Reservation reservation) {
        return ReservationCreateResponse.builder()
                .reservationPublicId(reservation.getPublicId())
                .build();
    }

    public static ReservationDetailDto toReservationDetailDto(Reservation reservation) {
        String username =  reservation.getUser().getUsername();
        String nickname =  reservation.getUser().getNickname();
        String phone = reservation.getUser().getPhone();

        return ReservationDetailDto.builder()
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .headCount(reservation.getHeadCount())
                .couponId(reservation.getCoupon().getId())
                .request(reservation.getRequest())
                .totalPrice(reservation.getTotalPrice())
                .process(reservation.getProcess())
                .build();
    }

}
