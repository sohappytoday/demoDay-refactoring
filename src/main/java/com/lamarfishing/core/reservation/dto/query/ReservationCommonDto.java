package com.lamarfishing.core.reservation.dto.query;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.user.domain.Grade;
import lombok.Builder;
import lombok.Getter;

/**
 * 출항 일정 상세보기에 사용되는 dto
 */
@Getter
@Builder
public class ReservationCommonDto {
    private String reservationPublicId;
    private String nickname;                //user field
    private Grade grade;                    //user field
    private Integer headCount;
    private Reservation.Process process;

    public ReservationCommonDto(String reservationPublicId, String nickname, Grade grade, Integer headCount, Reservation.Process process) {
        this.reservationPublicId = reservationPublicId;
        this.nickname = nickname;
        this.grade = grade;
        this.headCount = headCount;
        this.process = process;
    }
}
