package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;

    public ScheduleDetailResponse getScheduleDetail(String publicId){
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        ScheduleDetailDto scheduleDetailDto = ScheduleMapper.toScheduleDetailDto(schedule);

        Ship ship = schedule.getShip();
        ShipDetailDto shipDetailDto = ShipMapper.toShipDetailResponse(ship);

        List<ReservationCommonDto> reservations =
                reservationRepository.findBySchedule(schedule)
                        .stream()
                        .map(ReservationMapper::toReservationCommonDto)
                        .toList();

        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.from(shipDetailDto, scheduleDetailDto, reservations);

        return scheduleDetailResponse;
    }

}
