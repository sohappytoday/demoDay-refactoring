package com.lamarfishing.core.user.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.ProfileCouponDto;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.MyProfileDto;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.mapper.UserMapper;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_BASIC', 'GRADE_VIP')")
    public MyProfileDto getMyProfile(User user) {

        List<Coupon> coupons = couponRepository.findByUserAndStatus(user,Coupon.Status.AVAILABLE);
        
        List<ProfileCouponDto> profileCoupons = coupons.stream()
                .map(CouponMapper::toProfileCouponDto)
                .toList();

        return UserMapper.toMyProfileDto(user, profileCoupons);
    }

}
