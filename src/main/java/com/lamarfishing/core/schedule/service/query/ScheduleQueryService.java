package com.lamarfishing.core.schedule.service.query;

import com.lamarfishing.core.reservation.dto.query.ReservationCommonDto;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.dto.common.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.query.ScheduleDetailFlat;
import com.lamarfishing.core.schedule.dto.result.ScheduleDetailResult;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.schedule.resolver.ScheduleResolver;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
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
    private final ScheduleResolver scheduleResolver;

    public List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.getSchedules(from, to);
    }

    public ScheduleDetailResult getScheduleDetail(String publicId) {

        validateSchedulePublicId(publicId);

        //schedulePublicId -> scheduleId (추후 Redis 사용)
        Long scheduleId = scheduleResolver.resolve(publicId);

        ScheduleDetailFlat flatDto = scheduleRepository.findScheduleDetailFlat(scheduleId);

        ShipDetailDto shipDetailDto = ShipDetailDto.from(flatDto);
        ScheduleDetailDto scheduleDetailDto = ScheduleDetailDto.from(flatDto);
        List<ReservationCommonDto> reservations = reservationRepository.getReservations(scheduleId);

        return ScheduleDetailResult.of(shipDetailDto, scheduleDetailDto, reservations);
    }

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public DepartureTimeResult viewDepartureTime() {
        return scheduleRepository.findNextDeparture();
    }

}
