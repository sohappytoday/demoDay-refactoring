package com.lamarfishing.core.schedule.mapper;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.common.ReservationDetailScheduleDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
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
                .type(schedule.getType())
                .build();
    }

    public static ReservationDetailScheduleDto toReservationDetailScheduleDto(Schedule schedule) {
        return ReservationDetailScheduleDto.builder()
                .departure(schedule.getDeparture())
                .dayOfWeek(schedule.getDeparture().getDayOfWeek())
                .tide(schedule.getTide())
                .build();
    }

}
