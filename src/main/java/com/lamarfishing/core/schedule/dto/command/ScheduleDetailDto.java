package com.lamarfishing.core.schedule.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 출항 일정 상세 조회에 사용되는 dto
 */
@Getter
@Builder
public class ScheduleDetailDto {
    private String schedulePublicId;
    private LocalDateTime departure;
    private Integer tide;
    private Integer remainCount;

}
