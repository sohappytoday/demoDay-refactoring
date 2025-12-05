package com.lamarfishing.core.reservation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.lamarfishing.core.reservation.domain.Reservation.Process;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSimpleDto {

    String reservationId;
    Integer totalPrice;
    Process process;
    int headCount;
    String request;
    String username;
    String shipFishType;
    LocalDateTime scheduleDeparture;
}
