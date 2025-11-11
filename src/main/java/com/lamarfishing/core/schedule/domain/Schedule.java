package com.lamarfishing.core.schedule.domain;

import com.lamarfishing.core.common.uuid.Uuid;
import com.lamarfishing.core.manifest.domain.Manifest;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.ship.domain.Ship;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(name = "schedule_departure")
    private LocalDateTime departure;

    @Column(name = "schedule_current_head_count")
    private int currentHeadCount;

    @Column(name = "schedule_tide")
    private int tide;

    public enum Status {
        WAITING, // 출항 전
        CONFIRMED, // 출항 확정
        DELAYED, // 출항 보류
        CANCELED, // 출항 취소
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private Status status;

    public enum Type {
        EARLY, // 선 예약 오픈
        DRAWN, // 선 예약 추첨 완료
        NORMAL, // 일반 예약 오픈
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @Builder
    private Schedule(LocalDateTime departure, int currentHeadCount, int tide, Status status, Type type, Ship ship) {
        this.publicId = "sch"+Uuid.generateShortUUID();
        this.departure = departure;
        this.currentHeadCount = currentHeadCount;
        this.tide = tide;
        this.status = status;
        this.type = type;
        this.ship = ship;
    }

    public static Schedule create(LocalDateTime departure, int currentHeadCount, int tide, Status status, Type type, Ship ship) {
        return Schedule.builder()
                .departure(departure)
                .currentHeadCount(currentHeadCount)
                .tide(tide)
                .status(status)
                .type(type)
                .ship(ship)
                .build();
    }

    public void updateDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    // 문제 발생 가능성 존재
    public void changeHeadCount(int count) {
        if (currentHeadCount + count > ship.getMaxHeadCount()) {
            // 예외 발생
        }
        this.currentHeadCount += count;
    }

    public void decreaseCurrentHeadCount(int headCount) {
        if (currentHeadCount - headCount < 0) {
            //예외 발생
        }
        this.currentHeadCount -= headCount;
    }

    public void updateType(Schedule.Type newType) {
        this.type = newType;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void changeType(Type type) {
        this.type = type;
    }
}
