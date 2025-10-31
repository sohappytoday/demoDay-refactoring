package com.lamarfishing.core.coupon.repository;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    public List<Coupon> findByUser(User user);
}
