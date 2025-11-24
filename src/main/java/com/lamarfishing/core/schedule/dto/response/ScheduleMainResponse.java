package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ScheduleMainResponse {

    List<ScheduleMainDto> schedules;

    private ScheduleMainResponse(List<ScheduleMainDto> dto) {
        this.schedules = dto;
    }

    public static ScheduleMainResponse from(List<ScheduleMainDto> dto) {
        return new ScheduleMainResponse(dto);
    }
}
