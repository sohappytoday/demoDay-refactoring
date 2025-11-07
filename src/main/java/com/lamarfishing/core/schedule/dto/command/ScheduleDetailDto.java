package com.lamarfishing.core.schedule.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleDetailDto {
    private String schedulePublicId;
    private LocalDateTime departure;
    private Integer tide;
    private Integer remainCount;

}
