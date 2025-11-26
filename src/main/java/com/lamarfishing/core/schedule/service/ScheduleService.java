package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.request.UpdateDepartureTimeRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.dto.response.ViewDepartureTimeResponse;
import com.lamarfishing.core.schedule.exception.*;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.exception.ShipNotFound;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;

    public ScheduleDetailResponse getScheduleDetail(String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

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

        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.from(shipDetailDto, scheduleDetailDto, reservations);

        return scheduleDetailResponse;
    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void createSchedule(Long userId, LocalDate startDate, LocalDate endDate, Long shipId, Type scheduleType) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Ship ship = shipRepository.findById(shipId).orElseThrow(ShipNotFound::new);

        //중복되는 날짜가 있으면 덮어쓰기, 없으면 생성
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            Optional<Schedule> existingSchedule = scheduleRepository
                    .findFirstByDepartureBetween(startOfDay, endOfDay);

            //이미 날짜가 있다면
            if (existingSchedule.isPresent()) {
                existingSchedule.get().updateType(scheduleType);
                continue;
            }

            int tide = (date.getDayOfYear() % 15) + 1;

            Schedule schedule = Schedule.create(date.atTime(4, 0, 0), 0, tide, Status.WAITING, scheduleType, ship);
            scheduleRepository.save(schedule);

        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void deleteSchedule(Long userId, String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        boolean hasReservations = reservationRepository.existsBySchedule(schedule);
        if (hasReservations) {
            throw new ScheduleHasReservations();
        }

        scheduleRepository.delete(schedule);
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public ViewDepartureTimeResponse viewDepartureTime(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        LocalDateTime now = LocalDateTime.now();
        LocalDate todayDate = now.toLocalDate();

        LocalDateTime todayStart = todayDate.atStartOfDay();
        LocalDateTime todayEnd = todayDate.atTime(23, 59, 59);

        LocalDate tomorrowDate = todayDate.plusDays(1);
        LocalDateTime tomorrowStart = tomorrowDate.atStartOfDay();
        LocalDateTime tomorrowEnd = tomorrowDate.atTime(23, 59, 59);

        // 오늘 일정 존재
        if (scheduleRepository.existsByDepartureBetween(now, todayEnd)) {
            Schedule schedule = scheduleRepository
                    .findFirstByDepartureBetween(now, todayEnd)
                    .orElseThrow(ScheduleNotFound::new);

            return ViewDepartureTimeResponse.from(true, schedule);
        }

        // 내일 일정 존재
        if (scheduleRepository.existsByDepartureBetween(tomorrowStart, tomorrowEnd)) {
            Schedule schedule = scheduleRepository
                    .findFirstByDepartureBetween(tomorrowStart, tomorrowEnd)
                    .orElseThrow(ScheduleNotFound::new);

            return ViewDepartureTimeResponse.from(true, schedule);
        }

        // 둘 다 없으면 내일 날짜만 반환
        return ViewDepartureTimeResponse.from(false, tomorrowDate);
    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void updateDepartureTime(Long userId, String publicId, LocalTime updateTime) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);

        LocalDate departureDate = schedule.getDeparture().toLocalDate();
        LocalDateTime updateDeparture = departureDate.atTime(updateTime);

        schedule.updateDeparture(updateDeparture);
    }

//    /**
//     * 선예약 마감
//     */
//    @Transactional
//    public void markAsDrawn(Long userId, String publicId) {
//        if (!publicId.startsWith("sch")) {
//            throw new InvalidSchedulePublicId();
//        }
//
//        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
//        if (user.getGrade() != User.Grade.ADMIN) {
//            throw new InvalidUserGrade();
//        }
//
//        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
//        //선예약 시에만
//        if (schedule.getType() == Type.EARLY) {
//            schedule.changeType(Type.DRAWN);
//        }
//        throw new InvalidScheduleType();
//    }
}
