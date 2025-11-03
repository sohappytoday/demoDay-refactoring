package com.lamarfishing.core.log.message.dto.request;

import com.lamarfishing.core.schedule.domain.Schedule;
import lombok.Data;

@Data
public class DepartureRequest {
    private Schedule.Status scheduleStatus;

}
