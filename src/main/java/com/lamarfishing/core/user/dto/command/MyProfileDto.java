package com.lamarfishing.core.user.dto.command;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.ProfileCouponDto;
import com.lamarfishing.core.user.domain.Grade;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.lamarfishing.core.user.domain.User.*;

@Data
@Builder
public class MyProfileDto {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;
    private List<ProfileCouponDto> coupons;

    public MyProfileDto(String username, String nickname, Grade grade, String phone, List<ProfileCouponDto> coupons) {
        this.username = username;
        this.nickname = nickname;
        this.grade = grade;
        this.phone = phone;
        this.coupons = coupons;
    }
}
