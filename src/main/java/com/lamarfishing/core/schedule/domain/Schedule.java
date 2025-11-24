package com.lamarfishing.core.schedule.domain;

import com.lamarfishing.core.common.uuid.Uuid;
import com.lamarfishing.core.ship.domain.Ship;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private Status status;

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

    public void increaseCurrentHeadCount(int headCount) {
        this.currentHeadCount += headCount;
    }

    public void decreaseCurrentHeadCount(int headCount) {
        this.currentHeadCount -= headCount;
    }

    public void updateType(Type newType) {
        this.type = newType;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void changeType(Type type) {
        this.type = type;
    }
}
