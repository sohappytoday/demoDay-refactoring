package com.lamarfishing.core.schedule.dto.command;

import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScheduleMainDto {

    // schedule
    private String schedulePublicId;
    private LocalDateTime departure;
    private Integer remainingHeadCount;
    private Integer tide;
    private Status status;
    private Type type;

    // ship
    private String fishType;
    private Integer price;
}
