package com.lamarfishing.core.schedule.dto.common;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 예약 상세 조회에 사용되는 dto
 */
@Data
@Builder
public class ReservationDetailScheduleDto {
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;
}
