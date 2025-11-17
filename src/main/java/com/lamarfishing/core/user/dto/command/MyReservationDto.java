package com.lamarfishing.core.user.dto.command;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyReservationDto {

    private Long reservationId;
    private int totalPrice;
    private Process process;
    private String shipFishType;
    private LocalDateTime scheduleDeparture;
}
