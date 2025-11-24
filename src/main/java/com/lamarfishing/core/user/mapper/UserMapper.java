package com.lamarfishing.core.user.mapper;

import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.dto.ProfileCouponDto;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.MyProfileDto;
import com.lamarfishing.core.user.dto.command.EarlyReservationUserDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;

import java.util.List;

public class UserMapper {

    private static final String GUEST_USERNAME = "비회원_이름";
    private static final String GUEST_NICKNAME = "비회원_닉네임";
    private static final String GUEST_PHONE = "비회원_전화번호";

    public static MyProfileDto toMyProfileDto(User user, List<ProfileCouponDto> coupons) {
        return MyProfileDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .grade(user.getGrade())
                .phone(user.getPhone())
                .coupons(coupons)
                .build();
    }

    public static EarlyReservationUserDto toEarlyReservationUserDto(User user, List<CouponCommonDto> coupons) {
        return EarlyReservationUserDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .grade(user.getGrade())
                .phone(user.getPhone())
                .coupons(coupons)
                .build();
    }

    public static NormalReservationUserDto toNormalReservationUserDto(User user) {
        return NormalReservationUserDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .grade(user.getGrade())
                .phone(user.getPhone())
                .build();
    }

    //비회원용
    public static NormalReservationUserDto toNormalReservationUserDto(){
        return NormalReservationUserDto.builder()
                .username(GUEST_USERNAME)
                .nickname(GUEST_NICKNAME)
                .grade(Grade.GUEST)
                .phone(GUEST_PHONE)
                .build();
    }
}
