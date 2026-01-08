package com.lamarfishing.core.coupon.repository;

import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.user.domain.User;

import java.util.List;

public interface CouponRepositoryCustom {
    List<CouponCommonDto> getAvailableByUser(User user);
}
