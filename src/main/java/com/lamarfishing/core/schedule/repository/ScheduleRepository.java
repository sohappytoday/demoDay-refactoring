package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByPublicId(String publicId);
    Optional<Schedule> findFirstByDepartureBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByDepartureBetween(LocalDateTime start, LocalDateTime end);
}
