package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public Page<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return scheduleRepository.getSchedules(from, to, pageable);
    }
}
