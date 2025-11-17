package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ScheduleRepositoryCustom {
    public Page<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
