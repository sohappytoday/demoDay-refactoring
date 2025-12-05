package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.common.TodayScheduleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to);
    public TodayScheduleDto getTodaySchedule();
}
