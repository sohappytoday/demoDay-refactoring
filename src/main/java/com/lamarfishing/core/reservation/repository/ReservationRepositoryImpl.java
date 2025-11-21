package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.QReservation;
import com.lamarfishing.core.schedule.domain.QSchedule;
import com.lamarfishing.core.ship.domain.QShip;
import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.util.List;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReservationSimpleDto> getReservations(Long userId, Process process, Pageable pageable) {
        List<ReservationSimpleDto> mainQuery = queryFactory
                .select(Projections.constructor(ReservationSimpleDto.class,
                        QReservation.reservation.id, QReservation.reservation.totalPrice,
                        QReservation.reservation.process, QReservation.reservation.schedule.ship.fishType,
                        QReservation.reservation.schedule.departure))
                .from(QReservation.reservation)
                .leftJoin(QSchedule.schedule).on(QSchedule.schedule.id.eq(QReservation.reservation.schedule.id))
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(QReservation.reservation.schedule.id))
                .where(userIdEq(userId), processEq(process))
                .orderBy(QReservation.reservation.schedule.departure.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QReservation.reservation.count())
                .from(QReservation.reservation)
                .where(QReservation.reservation.user.id.eq(userId));

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }

    private BooleanExpression processEq(Process process) {
        return process == null ? null : QReservation.reservation.process.eq(process);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : QReservation.reservation.user.id.eq(userId);
    }

}
