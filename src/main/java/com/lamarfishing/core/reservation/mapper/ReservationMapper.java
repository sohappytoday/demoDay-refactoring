package com.lamarfishing.core.reservation.mapper;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.user.domain.User;

public class ReservationMapper {

    public static ReservationCommonDto toReservationCommonDto(Reservation reservation) {
        User.Grade grade = reservation.getUser().getGrade();
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
}
