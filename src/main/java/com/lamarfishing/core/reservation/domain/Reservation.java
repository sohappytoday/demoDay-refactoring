package com.lamarfishing.core.reservation.domain;

import com.lamarfishing.core.common.domain.BaseTimeEntity;
import com.lamarfishing.core.common.uuid.Uuid;
import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.reservation.exception.InvalidRequestContent;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(name = "reservation_head_count")
    private int headCount;

    @Column(name = "reservation_request")
    private String request;

    @Column(name = "reservation_total_price")
    private int totalPrice;

    public enum Process {
        RESERVE_COMPLETED, // 예약 완료
        DEPOSIT_COMPLETED, // 입금 완료
        CANCEL_REQUESTED, // 취소 신청
        CANCEL_COMPLETED // 취소 완료
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    private Reservation(int headCount, String request, int totalPrice, Process process, User user, Schedule schedule, Coupon coupon) {
        this.publicId = "res"+Uuid.generateShortUUID();
        this.headCount = headCount;
        this.request = request;
        this.totalPrice = totalPrice;
        this.process = process;
        this.user = user;
        this.schedule = schedule;
        this.coupon = coupon;
    }

    public static Reservation create(int headCount, String request, int totalPrice, Process process, User user, Schedule schedule, Coupon coupon) {
        return Reservation.builder()
                .headCount(headCount)
                .request(request)
                .totalPrice(totalPrice)
                .process(process)
                .user(user)
                .schedule(schedule)
                .coupon(coupon)
                .build();
    }

    // 상태 변경 메서드
    public void requestCancel() {
        if (this.process == Process.CANCEL_REQUESTED) {
            return;
        }
        if (!canRequestCancel()) {
            throw new InvalidRequestContent();
        }

        this.process = Process.CANCEL_REQUESTED;
    }

    public void completeDeposit(){
        if (this.process == Process.DEPOSIT_COMPLETED) {
            return;
        }

        if (!canCompleteDeposit()) {
            throw new InvalidRequestContent();
        }
        this.process = Process.DEPOSIT_COMPLETED;
    }

    public void completeCancel(){
        if (this.process == Process.CANCEL_COMPLETED) {
            return;
        }

        if (!canCompleteCancel()){
            throw new InvalidRequestContent();
        }
        this.process = Process.CANCEL_COMPLETED;
        this.schedule.decreaseCurrentHeadCount(this.headCount);
    }

    private boolean canRequestCancel() {
        return this.process == Process.RESERVE_COMPLETED;
    }

    private boolean canCompleteDeposit() {
        return this.process == Process.RESERVE_COMPLETED;
    }

    private boolean canCompleteCancel() {
        return this.process == Process.CANCEL_REQUESTED;
    }

}
