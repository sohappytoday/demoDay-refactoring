package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.exception.InvalidDepartureRequest;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import com.lamarfishing.core.schedule.dto.response.DepartureResponse;
import com.lamarfishing.core.message.service.MessageService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.validate.ValidatePublicId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartureService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    //출항 확정
    public DepartureResponse confirmation(Long userId, String publicId, Status scheduleStatus) {
        return processDeparture(userId, publicId, scheduleStatus, Status.CONFIRMED);
    }

    public DepartureResponse cancel(Long userId, String publicId, Status scheduleStatus) {
        return processDeparture(userId, publicId, scheduleStatus, Status.CANCELED);
    }

    public DepartureResponse delay(Long userId, String publicId, Status scheduleStatus) {
        return processDeparture(userId, publicId, scheduleStatus, Status.DELAYED);
    }

    private DepartureResponse processDeparture(Long userId, String publicId,
                                               Status requestStatus, Status expectedStatus) {

        if (requestStatus != expectedStatus) {
            throw new InvalidDepartureRequest();
        }

        ValidatePublicId.validateSchedulePublicId(publicId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

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
        List<MessageCommonDto> dto = messageService.sendMessage(phones, expectedStatus);

        return DepartureResponse.from(dto);
    }
}
