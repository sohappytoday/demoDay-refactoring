package com.lamarfishing.core.user.dto.query;

import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.user.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EarlyReservationUserDto {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;
    private List<CouponCommonDto> coupons;

}
