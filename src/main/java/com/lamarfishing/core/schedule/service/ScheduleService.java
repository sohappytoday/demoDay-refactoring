package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ScheduleDetailDto;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.exception.DuplicateSchedule;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
import com.lamarfishing.core.schedule.exception.ScheduleHasReservations;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.exception.ShipNotFound;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public ScheduleDetailResponse getScheduleDetail(String publicId){
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        ScheduleDetailDto scheduleDetailDto = ScheduleMapper.toScheduleDetailDto(schedule);

        Ship ship = schedule.getShip();
        ShipDetailDto shipDetailDto = ShipMapper.toShipDetailDto(ship);

        List<ReservationCommonDto> reservations =
                reservationRepository.findBySchedule(schedule)
                        .stream()
                        .map(ReservationMapper::toReservationCommonDto)
                        .toList();

        ScheduleDetailResponse scheduleDetailResponse = ScheduleDetailResponse.from(shipDetailDto, scheduleDetailDto, reservations);

        return scheduleDetailResponse;
    }

    @Transactional
    public void createSchedule(Long userId, ScheduleCreateRequest scheduleCreateRequest){
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if(user.getGrade()!=User.Grade.ADMIN){
            throw new InvalidUserGrade();
        }

        LocalDate startDate = scheduleCreateRequest.getStartDate();
        LocalDate endDate = scheduleCreateRequest.getEndDate();
        Long shipId = scheduleCreateRequest.getShipId();
        Schedule.Type scheduleType = scheduleCreateRequest.getScheduleType();

        Ship ship = shipRepository.findById(shipId).orElseThrow(ShipNotFound::new);
        Integer maxHeadCount = ship.getMaxHeadCount();
        //중복되는 날짜가 있으면 덮어쓰기, 없으면 생성
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            Optional<Schedule> existing = scheduleRepository
                    .findFirstByDepartureBetween(startOfDay, endOfDay);

            //이미 날짜가 있다면
            if (existing.isPresent()) {
                Schedule schedule = existing.get();
                schedule.updateType(scheduleType);
                continue;
            }

            int tide = (date.getDayOfYear() % 15) + 1;
            Schedule schedule = Schedule.create(date.atTime(4,0,0),maxHeadCount,tide,Schedule.Status.WAITING,scheduleType,ship);
            scheduleRepository.save(schedule);

        }
    }

    @Transactional
    public void deleteSchedule(Long userId, String publicId){
        if(!publicId.startsWith("sch")){
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if(user.getGrade()!=User.Grade.ADMIN){
            throw new InvalidUserGrade();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        boolean hasReservations = reservationRepository.existsBySchedule(schedule);
        if (hasReservations) {
            throw new ScheduleHasReservations();
        }

        scheduleRepository.delete(schedule);
    }

}
