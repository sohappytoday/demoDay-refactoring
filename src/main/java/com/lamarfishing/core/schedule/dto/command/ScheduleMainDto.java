package com.lamarfishing.core.schedule.dto.command;

import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.domain.Type;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleMainDto {

    // schedule
    private Long id;
    private LocalDateTime departure;
    private int remainingHeadCount;
    private int tide;
    private Status status;
    private Type type;

    // ship
    private String fishType;
    private int price;
}
