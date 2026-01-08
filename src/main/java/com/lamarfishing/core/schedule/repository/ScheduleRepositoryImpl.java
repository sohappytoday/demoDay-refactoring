package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.common.TodayScheduleDto;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import com.lamarfishing.core.ship.domain.QShip;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.lamarfishing.core.schedule.domain.QSchedule.schedule;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {

        List<ScheduleMainDto> mainQuery = queryFactory
                .select(Projections.constructor(ScheduleMainDto.class,
                        schedule.publicId, schedule.departure,
                        QShip.ship.maxHeadCount.subtract(schedule.currentHeadCount),
                        schedule.tide, schedule.status, schedule.type,
                        QShip.ship.fishType, QShip.ship.price))
                .from(schedule)
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(schedule.ship.id))
                .where(schedule.departure.after(from).and(schedule.departure.before(to)))
                .orderBy(schedule.departure.desc())
                .fetch();

        return mainQuery;
    }

    public TodayScheduleDto getTodaySchedule() {

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return  queryFactory
                .select(Projections.constructor(TodayScheduleDto.class,
                        schedule.currentHeadCount, schedule.departure,
                        schedule.status,
                        Projections.constructor(TodayScheduleDto.ShipDto.class,
                                schedule.ship.fishType, schedule.ship.price,
                                schedule.ship.notification, schedule.ship.maxHeadCount)))
                .from(schedule)
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(schedule.ship.id))
                .where(schedule.departure.goe(start), schedule.departure.lt(end))
                .orderBy(schedule.departure.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public DepartureTimeResult findNextDeparture() {
        return queryFactory.select(Projections.constructor(DepartureTimeResult.class,
                        schedule.publicId,
                        schedule.departure
                        ))
                .from(schedule)
                .where(schedule.departure.after(LocalDateTime.now()))
                .fetchFirst();
    }

}
