package com.lamarfishing.core.reservation.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationSimpleDto {

    private Long reservationId;
    private int totalPrice;
    private Process process;
    private String shipFishType;
    private LocalDateTime scheduleDeparture;
}
