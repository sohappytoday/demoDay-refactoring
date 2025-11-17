package com.lamarfishing.core.schedule.dto.request;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Status;
import lombok.Data;

@Data
public class DepartureRequest {
    private Status scheduleStatus;

}
