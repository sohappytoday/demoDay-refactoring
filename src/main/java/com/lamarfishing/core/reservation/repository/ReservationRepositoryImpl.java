package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.ship.domain.QShip;
import com.lamarfishing.core.reservation.dto.common.ReservationSimpleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;
import java.util.List;

import static com.lamarfishing.core.reservation.domain.QReservation.reservation;
import static com.lamarfishing.core.schedule.domain.QSchedule.schedule;
import static com.lamarfishing.core.user.domain.QUser.user;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 종윤이 코드
     */
    // 예약자 이름, 인원, 메모,
    @Override
    public Page<ReservationSimpleDto> getReservations(Long userId, Process process,
                                                      LocalDateTime from, LocalDateTime to,
                                                      Long shipId, Pageable pageable) {
        List<ReservationSimpleDto> mainQuery = queryFactory
                .select(Projections.constructor(ReservationSimpleDto.class,
                        reservation.publicId, reservation.totalPrice,
                        reservation.process, reservation.headCount, reservation.request,
                        reservation.user.username,
                        reservation.schedule.ship.fishType,
                        reservation.schedule.departure))
                .from(reservation)
                .leftJoin(schedule).on(schedule.id.eq(reservation.schedule.id))
                .leftJoin(QShip.ship).on(QShip.ship.id.eq(reservation.schedule.id))
                .where(userIdEq(userId), processEq(process), fromGoe(from), toLoe(to), shipIdEq(shipId))
                .orderBy(reservation.schedule.departure.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(userIdEq(userId), processEq(process), fromGoe(from), toLoe(to), shipIdEq(shipId));

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ReservationCommonDto> getReservations(Long id){
        return queryFactory.select(Projections.constructor(ReservationCommonDto.class,
                reservation.publicId,
                user.nickname,
                user.grade,
                reservation.headCount,
                reservation.process))
                .from(reservation)
                .join(reservation.user, user)
                .join(reservation.schedule, schedule)
                .where(schedule.id.eq(id),
                        reservation.process.ne(Process.CANCEL_COMPLETED))
                .orderBy(reservation.createdAt.asc())
                .fetch();

    }

    private BooleanExpression processEq(Process process) {
        return process == null ? null : reservation.process.eq(process);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId == null ? null : reservation.user.id.eq(userId);
    }

    private BooleanExpression fromGoe(LocalDateTime from) {
        return from == null ? null : reservation.schedule.departure.goe(from);
    }

    private BooleanExpression toLoe(LocalDateTime to) {
        return to == null ? null : reservation.schedule.departure.loe(to);
    }

    private BooleanExpression shipIdEq(Long shipId) {
        return shipId == null ? null : reservation.schedule.ship.id.eq(shipId);
    }

}
