package com.lamarfishing.core.log.admin.service;

import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;
import com.lamarfishing.core.log.statistic.repository.StatisticRepository;
import com.lamarfishing.core.schedule.dto.command.TodayScheduleDto;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ScheduleRepository scheduleRepository;

    public TodayScheduleDto getTodaySchedule() {
        return scheduleRepository.getTodaySchedule();
    }
}
