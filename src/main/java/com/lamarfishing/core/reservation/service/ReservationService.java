package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.request.ReservationProcessUpdateRequest;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.exception.InvalidRequestContent;
import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.command.ReservationDetailScheduleDto;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationDetailShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ReservationDetailResponse getReservationDetail(Long userId, String publicId) {
        if (!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        //관리자나 예약자가 아니면 거부
        if (!user.getGrade().equals(User.Grade.ADMIN) && !reservation.getUser().equals(user)) {
            throw new InvalidUserGrade();
        }

        Schedule schedule = reservation.getSchedule();
        Ship ship = schedule.getShip();

        ReservationDetailShipDto reservationDetailShipDto = ShipMapper.toReservationDetailShipDto(ship);
        ReservationDetailDto reservationDetailDto = ReservationMapper.toReservationDetailDto(reservation);
        ReservationDetailScheduleDto reservationDetailScheduleDto = ScheduleMapper.toReservationDetailScheduleDto(schedule);

        ReservationDetailResponse response = ReservationDetailResponse.from(reservationDetailShipDto, reservationDetailDto, reservationDetailScheduleDto);

        return response;
    }

    public void changeReservationProcess(Long userId, String publicId, ReservationProcessUpdateRequest request) {
        if (!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }

        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow(ReservationNotFound::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Reservation.Process requestProcess = request.getProcess();
        if (requestProcess == null) {
            throw new InvalidRequestContent();
        }
        //관리자가 아니면 requestProcess는 오직 CANCEL_REQUESTED
        if (user.getGrade() != User.Grade.ADMIN) {
            if (requestProcess != Reservation.Process.CANCEL_REQUESTED) {
                throw new InvalidRequestContent();
            }
            reservation.changeProcess(requestProcess);
            return;
        }

        if(requestProcess ==  Reservation.Process.CANCEL_REQUESTED) {
            throw new InvalidRequestContent();
        }
        reservation.changeProcess(requestProcess);;
    }
}
