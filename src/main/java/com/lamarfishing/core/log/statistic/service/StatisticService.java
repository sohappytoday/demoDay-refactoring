package com.lamarfishing.core.log.statistic.service;

import com.lamarfishing.core.log.statistic.domain.Statistic;
import com.lamarfishing.core.log.statistic.dto.MainInfoDto;
import com.lamarfishing.core.log.statistic.dto.StatisticDto;
import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;
import com.lamarfishing.core.log.statistic.repository.StatisticRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final ScheduleRepository scheduleRepository;


    public StatisticDto getStatistic() {
        return statisticRepository.getStatistic();
    }

    public MainInfoDto getMainInfo() {
        return statisticRepository.getMainInfo();
    }

    public TodayInfoDto getTodayInfo() {
        return statisticRepository.getTodayInfo();
    }

    public void addDeposited(LocalDate date) {
        Statistic statistic = statisticRepository.findByDate(date);
        statistic.addDeposited();
    }

    public void afterReservation(LocalDate date, String schedulePublicId, int headCount) {
        Statistic statistic = statisticRepository.findByDate(date);
        statistic.addReserved();

        Schedule schedule = scheduleRepository.findByPublicId(schedulePublicId).orElseThrow(ScheduleNotFound::new);
        int totalPrice = headCount * schedule.getShip().getPrice();
        statistic.addSales(totalPrice);
    }

    public void addVisited(LocalDate date) {
        Statistic statistic = statisticRepository.findByDate(date);
        statistic.addVisited();
    }
}
