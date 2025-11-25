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
        if (scheduleStatus != Status.CONFIRMED) {
            throw new InvalidDepartureRequest();
        }

        ValidatePublicId.validateSchedulePublicId(publicId);

        isAdminById(userId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        //출항 상태 변경
        schedule.changeStatus(Status.CONFIRMED);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User reservationUser = reservation.getUser();
            phones.add(reservationUser.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendMessage(phones, Status.CONFIRMED);

        return DepartureResponse.from(messageCommonDto);
    }

    //출항 취소
    public DepartureResponse cancel(Long userId, String publicId, Status scheduleStatus) {

        if (scheduleStatus != Status.CANCELED) {
            throw new InvalidDepartureRequest();
        }

        ValidatePublicId.validateSchedulePublicId(publicId);

        isAdminById(userId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        //출항 상태 변경
        schedule.changeStatus(Status.CANCELED);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User reservationUser = reservation.getUser();
            phones.add(reservationUser.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendMessage(phones, Status.CANCELED);

        return DepartureResponse.from(messageCommonDto);
    }

    //출항 연기
    public DepartureResponse delay(Long userId, String publicId, Status scheduleStatus) {

        if (scheduleStatus != Status.DELAYED) {
            throw new InvalidDepartureRequest();
        }

        ValidatePublicId.validateSchedulePublicId(publicId);

        isAdminById(userId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        //출항 상태 변경
        schedule.changeStatus(Status.DELAYED);

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);

        List<String> phones = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User reservationUser = reservation.getUser();
            phones.add(reservationUser.getPhone());
        }

        List<MessageCommonDto> messageCommonDto = messageService.sendMessage(phones, Status.DELAYED);

        return DepartureResponse.from(messageCommonDto);
    }

    private User isAdminById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if(user.getGrade() != Grade.ADMIN){
            throw new InvalidUserGrade();
        }
        return user;
    }

}
