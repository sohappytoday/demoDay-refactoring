package com.lamarfishing.core.log.statistic.repository;

import com.lamarfishing.core.log.statistic.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Long>, StatisticRepositoryCustom {
    Statistic findByDate();
}
