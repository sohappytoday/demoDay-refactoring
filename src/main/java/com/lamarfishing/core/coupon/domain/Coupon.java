package com.lamarfishing.core.coupon.domain;

import com.lamarfishing.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_expires_at")
    private LocalDateTime expiresAt;
    
    public enum Type {
        WEEKDAY, // 평일
        WEEKEND, // 주말
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Coupon(LocalDateTime expiresAt, Type type, User user) {
        this.expiresAt = expiresAt;
        this.type = type;
        this.user = user;
    }

    public static Coupon create(Type type, User user) {
        return new Coupon(LocalDateTime.now().plusDays(30), type, user);
    }

}
