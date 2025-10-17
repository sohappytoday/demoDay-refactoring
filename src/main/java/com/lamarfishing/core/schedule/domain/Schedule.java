package com.lamarfishing.core.schedule.domain;

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

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_departure")
    private LocalDateTime departure;

    @Column(name = "schedule_current_head_count")
    private int currentHeadCount;

    @Column(name = "schedule_tide")
    private int tide;

    @Column(name = "schedule_status")
    private Status status;

    @Column(name = "schedule_pivot")
    private int pivot;

    @Column(name = "schedule_type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @Builder
    private Schedule(LocalDateTime departure, int currentHeadCount, int tide, Status status, Type type, Ship ship) {
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

    public void changeDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public void changeHeadCount(int count) {
        if (currentHeadCount + count > ship.getMaxHeadCount()) {
            // 예외 발생
        }
        this.currentHeadCount += count;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void changeType(Type type) {
        this.type = type;
    }
}
