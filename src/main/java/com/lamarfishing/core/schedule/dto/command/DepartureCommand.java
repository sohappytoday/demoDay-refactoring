package com.lamarfishing.core.schedule.dto.command;

import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartureCommand {
    private Status scheduleStatus;

    public static DepartureCommand from(DepartureRequest departureRequest) {
        return DepartureCommand.builder()
                .scheduleStatus(departureRequest.getScheduleStatus())
                .build();
    }
}
