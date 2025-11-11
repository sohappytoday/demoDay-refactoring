package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.domain.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ViewDepartureTimeResponse {
    private boolean scheduleExist;
    private String schedulePublicId;
    private LocalDate date;
    private String dayOfWeek;
    private LocalTime dateTime;

    public static ViewDepartureTimeResponse from(Boolean scheduleExist, Schedule schedule){
        return ViewDepartureTimeResponse.builder()
                .scheduleExist(scheduleExist)
                .schedulePublicId(schedule.getPublicId())
                .date(schedule.getDeparture().toLocalDate())
                .dayOfWeek(schedule.getDeparture().toLocalDate().getDayOfWeek().toString())
                .dateTime(schedule.getDeparture().toLocalTime())
                .build();
    }

    public static ViewDepartureTimeResponse from(Boolean scheduleExist, LocalDate date){
        return ViewDepartureTimeResponse.builder()
                .scheduleExist(scheduleExist)
                .schedulePublicId(null)
                .date(date)
                .dayOfWeek(date.getDayOfWeek().toString())
                .dateTime(null)
                .build();
    }
}

