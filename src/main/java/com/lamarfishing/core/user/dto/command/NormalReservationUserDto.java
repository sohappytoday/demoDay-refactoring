package com.lamarfishing.core.user.dto.command;

import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NormalReservationUserDto {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;

}