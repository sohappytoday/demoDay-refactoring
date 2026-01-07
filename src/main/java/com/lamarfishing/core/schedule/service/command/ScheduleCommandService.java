package com.lamarfishing.core.schedule.service.command;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.dto.command.ScheduleCreateCommand;
import com.lamarfishing.core.schedule.dto.command.UpdateDepartureTimeCommand;
import com.lamarfishing.core.schedule.exception.ScheduleHasReservations;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.exception.ShipNotFound;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleCommandService {

    private final ScheduleRepository scheduleRepository;
    private final ShipRepository shipRepository;
    private final ReservationRepository reservationRepository;

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void createSchedule(ScheduleCreateCommand command) {

        Ship ship = shipRepository.findById(command.getShipId()).orElseThrow(ShipNotFound::new);

        //중복되는 날짜가 있으면 덮어쓰기, 없으면 생성
        for (LocalDate date = command.getStartDate(); !date.isAfter(command.getEndDate()); date = date.plusDays(1)) {

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            Optional<Schedule> existingSchedule = scheduleRepository
                    .findFirstByDepartureBetween(startOfDay, endOfDay);

            //이미 날짜가 있다면
            if (existingSchedule.isPresent()) {
                existingSchedule.get().updateType(command.getScheduleType());
                continue;
            }

            int tide = (date.getDayOfYear() % 15) + 1;

            Schedule schedule = Schedule.create(date.atTime(4, 0, 0), 0, tide, Status.WAITING, command.getScheduleType(), ship);
            scheduleRepository.save(schedule);

        }
    }

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void deleteSchedule( String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        boolean hasReservations = reservationRepository.existsBySchedule(schedule);
        if (hasReservations) {
            throw new ScheduleHasReservations();
        }

        scheduleRepository.delete(schedule);
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void updateDepartureTime(String publicId, UpdateDepartureTimeCommand command) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        LocalDate departureDate = schedule.getDeparture().toLocalDate();
        LocalDateTime updateDeparture = departureDate.atTime(command.getDepartureTime());

        schedule.updateDeparture(updateDeparture);
    }
}
