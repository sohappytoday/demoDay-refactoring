package com.lamarfishing.core.reservation.dto.command;
import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Getter;

import static com.lamarfishing.core.user.domain.User.*;

/**
 * 출항 일정 상세보기에 사용되는 dto
 */
@Getter
@Builder
public class ReservationCommonDto {
    private Long reservationId;
    private String reservationPublicId;
    private String nickname;                //user field
    private Grade grade;                    //user field
    private Integer headCount;
    private Reservation.Process process;
}
