package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.getSchedules(from, to);
    }
}
