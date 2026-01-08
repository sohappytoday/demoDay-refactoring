package com.lamarfishing.core.coupon.mapper;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.coupon.dto.ProfileCouponDto;

public class CouponMapper {
    public static CouponCommonDto toCouponCommonDto(Coupon coupon) {
        return CouponCommonDto.builder()
                .couponId(coupon.getId())
                .type(coupon.getType())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }

    public static ProfileCouponDto toProfileCouponDto(Coupon coupon) {
        return ProfileCouponDto.builder()
                .type(coupon.getType())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }
}
