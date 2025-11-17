package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.user.dto.command.MyReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public Page<MyReservationDto> getReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getMyReservations(userId, process, pageable);
    }
}
