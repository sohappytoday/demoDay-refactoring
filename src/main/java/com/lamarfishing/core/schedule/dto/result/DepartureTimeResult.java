package com.lamarfishing.core.schedule.dto.result;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartureTimeResult {

    private String schedulePublicId;
    private LocalDateTime date;

    public DepartureTimeResult(String schedulePublicId, LocalDateTime date) {
        this.schedulePublicId = schedulePublicId;
        this.date = date;
    }
}
