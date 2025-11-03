package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.log.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.schedule.exception.InvalidDepartureRequest;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import com.lamarfishing.core.schedule.dto.response.DepartureResponse;
import com.lamarfishing.core.log.message.service.MessageService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.exception.ScheduleInvalidPublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.user.domain.User;
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
    private final MessageService messageService;

    //출항 확정
    public DepartureResponse confirmation(String publicId, DepartureRequest departureRequest) {
        Schedule.Status scheduleStatus = departureRequest.getScheduleStatus();
        if (scheduleStatus != Schedule.Status.CONFIRMED) {
            throw new InvalidDepartureRequest();
        }
        if (!publicId.startsWith("sch")) {
            throw new ScheduleInvalidPublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        //출항 상태 변경
        schedule.changeStatus(Schedule.Status.CONFIRMED);
        scheduleRepository.save(schedule);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User user = reservation.getUser();
            phones.add(user.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendDepartureConfirmedMessages(phones);

        return DepartureResponse.from(messageCommonDto);
    }

    //출항 취소
    public DepartureResponse cancel(String publicId, DepartureRequest departureRequest) {
        Schedule.Status scheduleStatus = departureRequest.getScheduleStatus();
        if (scheduleStatus != Schedule.Status.CANCELED) {
            throw new InvalidDepartureRequest();
        }
        if (!publicId.startsWith("sch")) {
            throw new ScheduleInvalidPublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        //출항 상태 변경
        schedule.changeStatus(Schedule.Status.CANCELED);
        scheduleRepository.save(schedule);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User user = reservation.getUser();
            phones.add(user.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendDepartureCanceledMessages(phones);

        return DepartureResponse.from(messageCommonDto);
    }

    //출항 연기
    public DepartureResponse delay(String publicId, DepartureRequest departureRequest) {
        Schedule.Status scheduleStatus = departureRequest.getScheduleStatus();
        if (scheduleStatus != Schedule.Status.DELAYED) {
            throw new InvalidDepartureRequest();
        }
        if (!publicId.startsWith("sch")) {
            throw new ScheduleInvalidPublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        //출항 상태 변경
        schedule.changeStatus(Schedule.Status.DELAYED);
        scheduleRepository.save(schedule);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User user = reservation.getUser();
            phones.add(user.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendDepartureDelayedMessages(phones);

        return DepartureResponse.from(messageCommonDto);
    }

}
