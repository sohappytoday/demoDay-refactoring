package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Boolean existsBySchedule(Schedule schedule);
    List<Reservation> findBySchedule(Schedule schedule);
    Optional<Reservation> findByPublicId(String publicId);
}
