package com.lamarfishing.core.user.domain;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_username")
    private String username;

    @Column(name = "user_nickname", unique = true)
    private String nickname;

    public enum Grade {
        GUEST, BASIC, VIP, ADMIN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "user_grade")
    private Grade grade;

    @Column(name = "user_phone", unique = true)
    private String phone;

    private User(String username, String nickname, Grade grade, String phone) {
        this.username = username;
        this.nickname = nickname;
        this.grade = grade;
        this.phone = phone;
    }

    public static User create(String username, String nickname, Grade grade, String phone) {
        return new User(username, nickname, grade, phone);
    }
}
