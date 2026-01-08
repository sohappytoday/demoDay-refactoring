package com.lamarfishing.core.ship.repository;

import com.lamarfishing.core.schedule.domain.QSchedule;
import com.lamarfishing.core.ship.domain.QShip;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.lamarfishing.core.schedule.domain.QSchedule.schedule;
import static com.lamarfishing.core.ship.domain.QShip.ship;

@RequiredArgsConstructor
public class ShipRepositoryImpl implements ShipRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ShipDetailDto> getShips(Pageable pageable){
        List<ShipDetailDto> mainQuery = queryFactory
                .select(Projections.constructor(ShipDetailDto.class,
                        ship.id,
                        ship.fishType,
                        ship.price,
                        ship.maxHeadCount,
                        ship.notification
                ))
                .from(ship)
                .orderBy(ship.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(ship.count())
                .from(ship);

        return PageableExecutionUtils.getPage(mainQuery, pageable, countQuery::fetchOne);
    }


}
