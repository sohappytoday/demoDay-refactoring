package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public Page<ReservationSimpleDto> getMyReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getReservations(userId, process, pageable);
    }

    public Page<ReservationSimpleDto> getReservations(Process process, Pageable pageable) {
        return reservationRepository.getReservations(null, process, pageable);
    }
}
