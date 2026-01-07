package com.lamarfishing.core.coupon.domain;

import com.lamarfishing.core.common.domain.BaseTimeEntity;
import com.lamarfishing.core.coupon.exception.InvalidCouponStatus;
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
public class Coupon extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Coupon(LocalDateTime expiresAt, Type type, User user, Status status) {
        this.expiresAt = expiresAt;
        this.type = type;
        this.user = user;
        this.status = status;
    }

    public static Coupon create(Type type, User user) {
        return new Coupon(LocalDateTime.now().plusDays(30), type, user,  Status.AVAILABLE);
    }

    //쿠폰 사용
    public void use() {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new InvalidCouponStatus(); // 기간 만료
        }
        if (status != Status.AVAILABLE) {
            throw new InvalidCouponStatus(); // 상태 비정상
        }
        this.status = Status.USED;
    }

    //만료
    public void expire() {
        if (this.status == Status.AVAILABLE && this.expiresAt.isBefore(LocalDateTime.now())) {
            this.status = Status.EXPIRED;
        }
    }

    //더미 데이터 입력용 생성 메서드
    public static Coupon createDummy(LocalDateTime expiresAt, Type type, User user, Status status) {
        return new Coupon(expiresAt, type, user, status);
    }
}
