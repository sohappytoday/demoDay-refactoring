package com.lamarfishing.core.schedule.service.query;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.common.ReservationCommonDto;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.result.ScheduleDetailResult;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.lamarfishing.core.common.validate.ValidatePublicId.validateSchedulePublicId;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;

    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.getSchedules(from, to);
    }

    public ScheduleDetailResult getScheduleDetail(String publicId) {

        validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        ScheduleDetailDto scheduleDetailDto = ScheduleMapper.toScheduleDetailDto(schedule);

        Ship ship = schedule.getShip();
        ShipDetailDto shipDetailDto = ShipMapper.toShipDetailDto(ship);

        List<ReservationCommonDto> reservations =
                reservationRepository.findBySchedule(schedule)
                        .stream()
                        .filter(reservation -> reservation.getProcess() != Reservation.Process.CANCEL_COMPLETED)
                        .map(ReservationMapper::toReservationCommonDto)
                        .toList();

        return ScheduleDetailResult.of(shipDetailDto, scheduleDetailDto, reservations);
    }

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public DepartureTimeResult viewDepartureTime() {
        return scheduleRepository.findNextDeparture();
    }
}
