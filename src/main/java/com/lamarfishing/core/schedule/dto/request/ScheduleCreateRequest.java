package com.lamarfishing.core.schedule.dto.request;

import com.lamarfishing.core.schedule.domain.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ScheduleCreateRequest {
    private LocalDate startDate;    //예약 시작 날짜
    private LocalDate endDate;      //예약
    private Long shipId;
    private Schedule.Type scheduleType;

}
