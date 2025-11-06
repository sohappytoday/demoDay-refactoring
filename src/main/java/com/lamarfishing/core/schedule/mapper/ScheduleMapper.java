package com.lamarfishing.core.schedule.mapper;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ScheduleCommonDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleMapper {
    public static ScheduleCommonDto toScheduleCommonDto(Schedule schedule) {
        int maxHeadCount = schedule.getShip().getMaxHeadCount();

        return ScheduleCommonDto.builder()
                .schedulePublicId(schedule.getPublicId())
                .departure(schedule.getDeparture())
                .tide(schedule.getTide())
                .remainCount(maxHeadCount-schedule.getCurrentHeadCount())
                .build();
    }
}
