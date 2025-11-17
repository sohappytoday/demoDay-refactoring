package com.lamarfishing.core.reservation.repository;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.user.dto.command.MyReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

public interface ReservationRepositoryCustom {
    public Page<MyReservationDto> getMyReservations(Long userId, Process process, Pageable pageable);
}
