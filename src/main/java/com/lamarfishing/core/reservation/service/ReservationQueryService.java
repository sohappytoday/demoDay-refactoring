package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationSimpleDto> getMyReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getReservations(userId, process, null, null, null, pageable);
    }

    public Page<ReservationSimpleDto> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable) {
        return reservationRepository.getReservations(null, process, from, to, shipId, pageable);
    }
}
