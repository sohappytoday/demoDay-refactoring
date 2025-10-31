package com.lamarfishing.core.user.mapper;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.MyProfileDto;

import java.util.List;

public class UserMapper {

    public static MyProfileDto toMyProfileDto(User user, List<Coupon> coupons) {
        return MyProfileDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .grade(user.getGrade())
                .phone(user.getPhone())
                .coupons(coupons)
                .build();
    }
}
