package com.lamarfishing.core.schedule.service.command;

import com.lamarfishing.core.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.dto.command.DepartureCommand;
import com.lamarfishing.core.schedule.dto.result.DepartureResult;
import com.lamarfishing.core.schedule.exception.InvalidDepartureRequest;
import com.lamarfishing.core.message.service.command.MessageCommandService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartureCommandService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final MessageCommandService messageCommandService;

    //출항 확정
    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public DepartureResult confirmation(String publicId, DepartureCommand command) {
        return processDeparture(publicId,command.getScheduleStatus() , Status.CONFIRMED);
    }

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public DepartureResult cancel(String publicId, DepartureCommand command) {
        return processDeparture(publicId, command.getScheduleStatus(), Status.CANCELED);
    }

    // @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public DepartureResult delay(String publicId, DepartureCommand command) {
        return processDeparture(publicId, command.getScheduleStatus(), Status.DELAYED);
    }

    private DepartureResult processDeparture(String publicId,
                                               Status requestStatus, Status expectedStatus) {

        if (requestStatus != expectedStatus) {
            throw new InvalidDepartureRequest();
        }

        ValidatePublicId.validateSchedulePublicId(publicId);

        // 스케줄 조회
        Schedule schedule = scheduleRepository.findByPublicId(publicId)
                .orElseThrow(ScheduleNotFound::new);

        // 상태 변경
        schedule.changeStatus(expectedStatus);

        // 예약 조회
        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        // 폰 번호 추출
        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            phones.add(reservation.getUser().getPhone());
        }

        // 메시지 발송
        List<MessageCommonDto> dto = messageCommandService.sendMessage(phones, expectedStatus);

        return DepartureResult.from(dto);
    }
}
