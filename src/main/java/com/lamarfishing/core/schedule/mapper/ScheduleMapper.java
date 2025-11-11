package com.lamarfishing.core.schedule.mapper;

import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ReservationDetailScheduleDto;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.response.ViewDepartureTimeResponse;
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

    public static ReservationDetailScheduleDto toReservationDetailScheduleDto(Schedule schedule) {
        return ReservationDetailScheduleDto.builder()
                .departure(schedule.getDeparture())
                .dayOfWeek(schedule.getDeparture().getDayOfWeek())
                .tide(schedule.getTide())
                .build();
    }

    public static ViewDepartureTimeResponse toViewDepartureTimeResponse(Schedule schedule) {
        return ViewDepartureTimeResponse.builder()
                .schedulePublicId(schedule.getPublicId())
                .departureTime(schedule.getDeparture())
                .build();
    }
}
