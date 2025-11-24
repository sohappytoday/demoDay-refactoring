package com.lamarfishing.core.reservation.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
