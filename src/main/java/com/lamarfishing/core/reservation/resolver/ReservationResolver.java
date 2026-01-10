package com.lamarfishing.core.reservation.resolver;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationResolver {

    private final ReservationRepository reservationRepository;

    public Long resolve(String publicId) {
        return reservationRepository.findIdByPublicId(publicId).orElseThrow(ReservationNotFound::new);
    }
}
