package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.reservation.dto.common.ReservationSimpleDto;
import com.lamarfishing.core.reservation.dto.query.ReservationDetailFlat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {
    Page<ReservationSimpleDto> getReservations(Long userId, Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable);
    List<ReservationCommonDto> getReservations(Long id);
    ReservationDetailFlat getReservationDetail(Long reservationId);

    List<Reservation> findReservationBetweenDeparture(LocalDateTime start, LocalDateTime end);
    Long countReservationBetweenDeparture(LocalDateTime start, LocalDateTime end);

    List<String> findPhonesByDeparture(LocalDateTime start, LocalDateTime end);
    Optional<LocalDateTime> findDeparture(LocalDateTime start, LocalDateTime end);
    List<Reservation> findReservationByDeparture(LocalDateTime start, LocalDateTime end);
}
