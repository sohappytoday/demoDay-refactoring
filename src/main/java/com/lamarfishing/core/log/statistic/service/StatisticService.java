package com.lamarfishing.core.log.statistic.service;

import com.lamarfishing.core.log.statistic.dto.MainInfoDto;
import com.lamarfishing.core.log.statistic.dto.StatisticDto;
import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;
import com.lamarfishing.core.log.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticDto getStatistic() {
        return statisticRepository.getStatistic();
    }

    public MainInfoDto getMainInfo() {
        return statisticRepository.getMainInfo();
    }

    public TodayInfoDto getTodayInfo() {
        return statisticRepository.getTodayInfo();
    }
}
