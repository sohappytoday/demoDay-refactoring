package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.response.ReservationPopupResponse;
import com.lamarfishing.core.schedule.exception.ScheduleInvalidPublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.ReservationUserDto;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.mapper.UserMapper;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationPopupService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public ReservationPopupResponse getReservationPopup(Long userId, String grade, String publicId) {

        ReservationUserDto reservationUserDto;

        if (!publicId.startsWith("sch")) {
            throw new ScheduleInvalidPublicId();
        }

        User.Grade userGrade;
        try {
            userGrade = User.Grade.valueOf(grade.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserGrade();
        }

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        int currentHeadCount = schedule.getCurrentHeadCount();
        int remainHeadCount = ship.getMaxHeadCount() - currentHeadCount;

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipResponse(ship);

        //유저라면
        if (userGrade != User.Grade.GUEST) {
            User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

            List<CouponCommonDto> couponCommonDtos = couponRepository.findByUser(user)
                    .stream()
                    .map(CouponMapper::toCouponCommonDto)
                    .toList();

            reservationUserDto = UserMapper.toReservationUserDto(user, couponCommonDtos);
        } else {
            reservationUserDto = UserMapper.toReservationUserDto();
        }

        return ReservationPopupResponse.from(schedule, remainHeadCount, reservationUserDto, reservationShipDto);
    }

//    public ReservationCreateResponse getReservationCreateResponse(Long userId, String grade, String publicId) {
//        if (!publicId.startsWith("sch")) {
//            throw new ScheduleInvalidPublicId();
//        }
//
//    }
}
