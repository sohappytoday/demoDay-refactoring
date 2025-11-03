package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
