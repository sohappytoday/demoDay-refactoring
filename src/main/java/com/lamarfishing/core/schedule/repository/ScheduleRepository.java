package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    Optional<Schedule> findByPublicId(String publicId);
    Optional<Schedule> findFirstByDepartureBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByDepartureBetween(LocalDateTime start, LocalDateTime end);

    @Query("select s from Schedule s join fetch s.ship where s.publicId = :publicId")
    Optional<Schedule> findScheduleWithShip(@Param("publicId") String publicId);
}
