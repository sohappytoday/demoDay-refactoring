package com.lamarfishing.core.coupon.mapper;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;

public class CouponMapper {
    public static CouponCommonDto toCouponCommonDto(Coupon coupon) {
        return CouponCommonDto.builder()
                .couponId(coupon.getId())
                .type(coupon.getType())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }
}
