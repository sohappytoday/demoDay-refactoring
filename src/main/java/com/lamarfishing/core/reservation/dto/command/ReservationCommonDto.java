package com.lamarfishing.core.reservation.dto.command;
import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Getter;

import static com.lamarfishing.core.user.domain.User.*;

@Getter
@Builder
public class ReservationCommonDto {

    private String reservationPublicId;
    private String nickname;                //user field
    private Grade grade;                    //user field
    private Integer headCount;
    private Reservation.Process process;
}
