package com.lamarfishing.core.coupon.repository;

import com.lamarfishing.core.coupon.domain.Coupon.Status;
import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.lamarfishing.core.coupon.domain.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CouponCommonDto> findAvailableByUserId(Long userId){
        List<CouponCommonDto> mainQuery = queryFactory
                .select(Projections.constructor(CouponCommonDto.class,
                        coupon.id,
                        coupon.type,
                        coupon.expiresAt))
                .from(coupon)
                .where(
                        coupon.user.id.eq(userId),
                        coupon.status.eq(Status.AVAILABLE),
                        coupon.expiresAt.after(LocalDateTime.now()))
                .fetch();

        return mainQuery;
    }
}
