package com.lamarfishing.core.schedule.mapper;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleMapper {
    public static ScheduleDetailDto toScheduleDetailDto(Schedule schedule) {
        int maxHeadCount = schedule.getShip().getMaxHeadCount();

        return ScheduleDetailDto.builder()
                .schedulePublicId(schedule.getPublicId())
                .departure(schedule.getDeparture())
                .tide(schedule.getTide())
                .remainCount(maxHeadCount-schedule.getCurrentHeadCount())
                .build();
    }
}
