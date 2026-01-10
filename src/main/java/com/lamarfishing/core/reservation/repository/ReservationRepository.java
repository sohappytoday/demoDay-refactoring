package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    Boolean existsBySchedule(Schedule schedule);
    List<Reservation> findBySchedule(Schedule schedule);
    Optional<Reservation> findByPublicId(String publicId);
    List<Reservation> findByScheduleAndProcess(Schedule schedule, Reservation.Process process);

    @Query("select r.id from Reservation r where r.publicId = :publicId")
    Optional<Long> findIdByPublicId(@Param("publicId") String publicId);

    Boolean existsByIdAndUserId(Long reservationId, Long userId);
}
