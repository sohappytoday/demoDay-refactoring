package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.reservation.dto.common.ReservationSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {
    Page<ReservationSimpleDto> getReservations(Long userId, Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable);
    List<ReservationCommonDto> getReservations(Long id);
}
