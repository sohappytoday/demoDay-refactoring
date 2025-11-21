package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.domain.QSchedule;
import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import com.lamarfishing.core.ship.domain.QShip;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {

        List<ScheduleMainDto> mainQuery = queryFactory
                .select(Projections.constructor(ScheduleMainDto.class,
                        QSchedule.schedule.id, QSchedule.schedule.departure,
                        QShip.ship.maxHeadCount.subtract(QSchedule.schedule.currentHeadCount),
                        QSchedule.schedule.tide, QSchedule.schedule.status, QSchedule.schedule.type,
                        QShip.ship.fishType, QShip.ship.price))
                .from(QSchedule.schedule)
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(QSchedule.schedule.ship.id))
                .where(QSchedule.schedule.departure.after(from).and(QSchedule.schedule.departure.before(to)))
                .orderBy(QSchedule.schedule.departure.desc())
                .fetch();

        return mainQuery;
    }
}
