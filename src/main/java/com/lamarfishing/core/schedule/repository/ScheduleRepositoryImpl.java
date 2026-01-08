package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.common.TodayScheduleDto;
import com.lamarfishing.core.schedule.dto.query.EarlyReservationPopupFlatDto;
import com.lamarfishing.core.schedule.dto.query.ScheduleDetailFlat;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import com.lamarfishing.core.ship.domain.QShip;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.lamarfishing.core.schedule.domain.QSchedule.schedule;
import static com.lamarfishing.core.ship.domain.QShip.ship;
import static com.lamarfishing.core.user.domain.QUser.user;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {

        List<ScheduleMainDto> mainQuery = queryFactory
                .select(Projections.constructor(ScheduleMainDto.class,
                        schedule.publicId, schedule.departure,
                        ship.maxHeadCount.subtract(schedule.currentHeadCount),
                        schedule.tide, schedule.status, schedule.type,
                        ship.fishType, ship.price))
                .from(schedule)
                .leftJoin(ship).on(ship.id.eq(schedule.ship.id))
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
                .leftJoin(ship).on(ship.id.eq(schedule.ship.id))
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

    @Override
    public ScheduleDetailFlat findScheduleDetailFlat(Long id){

        NumberExpression<Integer> remainHeadcount =
                ship.maxHeadCount.subtract(schedule.currentHeadCount);

        return queryFactory.select(Projections.constructor(ScheduleDetailFlat.class,
                ship.id, ship.fishType, ship.price, ship.maxHeadCount, ship.notification,
                schedule.publicId, schedule.departure, schedule.tide, remainHeadcount, schedule.type)
            )
                .from(schedule)
                .join(schedule.ship, ship)
                .where(schedule.id.eq(id))
                .fetchOne();
    }

    @Override
    public EarlyReservationPopupFlatDto getScheduleAndShipPopup(Long scheduleId) {
        return queryFactory.select(Projections.constructor(EarlyReservationPopupFlatDto.class,
                        ship.fishType,
                        ship.price,
                        ship.notification,
                        schedule.publicId,
                        schedule.type,
                        ship.maxHeadCount.subtract(schedule.currentHeadCount),
                        schedule.departure,
                        schedule.tide
                ))
                .from(schedule)
                .join(schedule.ship, ship)
                .where(schedule.id.eq(scheduleId))
                .fetchOne();
    }



}
