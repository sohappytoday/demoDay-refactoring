package com.lamarfishing.core.log.message.dto.service;

import com.lamarfishing.core.log.message.dto.request.DepartureRequest;
import com.lamarfishing.core.log.message.dto.response.DepartureResponse;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.exception.ScheduleInvalidPublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;

    public DepartureResponse confirmation(String publicId, DepartureRequest departureRequest) {
        if(!publicId.startsWith("sch")){
            throw new ScheduleInvalidPublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Reservation reservation = reservationRepository.findBySchedule

    }
}
