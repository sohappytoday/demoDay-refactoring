package com.lamarfishing.core.user.dto.response;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.user.dto.command.MyProfileDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.lamarfishing.core.user.domain.User.Grade;

@Data
@Builder
public class MyProfileResponse {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;
    private List<Coupon> coupons;

    public static MyProfileResponse from(MyProfileDto dto) {
        return MyProfileResponse.builder()
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .grade(dto.getGrade())
                .phone(dto.getPhone())
                .coupons(dto.getCoupons())
                .build();
    }
}
