package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

public interface ReservationRepositoryCustom {
    public Page<ReservationSimpleDto> getReservations(Long userId, Process process, Pageable pageable);
}
