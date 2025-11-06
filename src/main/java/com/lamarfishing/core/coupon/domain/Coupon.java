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

    public enum Status {
        AVAILABLE, // 사용 가능
        USED,      // 이미 사용됨
        EXPIRED    // 기간 만료됨
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status")
    private Status status = Status.AVAILABLE; // 기본값


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Coupon(LocalDateTime expiresAt, Type type, User user) {
        this.expiresAt = expiresAt;
        this.type = type;
        this.user = user;
        this.status = Status.AVAILABLE;
    }

    public static Coupon create(Type type, User user) {
        return new Coupon(LocalDateTime.now().plusDays(30), type, user);
    }

    //쿠폰 사용
    public void use() {
        if (this.status != Status.AVAILABLE) {
            throw new IllegalStateException("이미 사용되었거나 만료된 쿠폰입니다.");
        }
        if (this.expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("쿠폰이 만료되었습니다.");
        }
        this.status = Status.USED;
    }
}
