package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.command.TodayScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to);
    public TodayScheduleDto getTodaySchedule();
}
