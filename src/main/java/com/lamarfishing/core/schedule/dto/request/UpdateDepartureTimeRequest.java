package com.lamarfishing.core.schedule.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

/**
 * 출항 시각 수정 API
 */
@Data
@Builder
public class UpdateDepartureTimeRequest {
    private LocalTime departureTime;
}
