package com.lamarfishing.core.schedule.dto.request;

import com.lamarfishing.core.schedule.domain.Schedule;
import lombok.Data;

@Data
public class DepartureRequest {
    private Schedule.Status scheduleStatus;

}
