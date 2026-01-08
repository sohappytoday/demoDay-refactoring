package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class DepartureTimeResponse {
    private boolean hasSchedule;
    private String schedulePublicId;
    private LocalDate date;
    private String dayOfWeek;
    private LocalTime dateTime;

    public static DepartureTimeResponse from(DepartureTimeResult result) {

        if (result == null){
            return DepartureTimeResponse.builder()
                    .hasSchedule(false)
                    .build();
        }

        return DepartureTimeResponse.builder()
                .hasSchedule(true)
                .schedulePublicId(result.getSchedulePublicId())
                .date(result.getDate().toLocalDate())
                .dayOfWeek(result.getDate().getDayOfWeek().toString())
                .dateTime(result.getDate().toLocalTime())
                .build();
    }
}

