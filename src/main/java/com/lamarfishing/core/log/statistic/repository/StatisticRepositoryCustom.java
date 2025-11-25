package com.lamarfishing.core.log.statistic.repository;

import com.lamarfishing.core.log.statistic.domain.Statistic;
import com.lamarfishing.core.log.statistic.dto.MainInfoDto;
import com.lamarfishing.core.log.statistic.dto.StatisticDto;
import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;

public interface StatisticRepositoryCustom {
    TodayInfoDto getTodayInfo();
    StatisticDto getStatistic();
    MainInfoDto getMainInfo();
}
