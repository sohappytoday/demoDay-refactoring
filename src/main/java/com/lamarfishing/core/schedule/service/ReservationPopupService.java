package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.exception.CouponNotFound;
import com.lamarfishing.core.coupon.exception.UnauthorizedCouponAccess;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationPopupResponse;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * parameter userId만 받게 fix 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationPopupService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ReservationRepository reservationRepository;

    public ReservationPopupResponse getReservationPopup(Long userId, String grade, String publicId) {

        ReservationUserDto reservationUserDto;

        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User.Grade userGrade = parseUserGrade(grade);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();

        int currentHeadCount = schedule.getCurrentHeadCount();
        int remainHeadCount = ship.getMaxHeadCount() - currentHeadCount;

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(ship);

        //비회원이라면
        if (userGrade == User.Grade.GUEST){
            reservationUserDto = UserMapper.toReservationUserDto();
            return ReservationPopupResponse.from(schedule, remainHeadCount, reservationUserDto, reservationShipDto);
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        List<CouponCommonDto> couponCommonDtos = couponRepository.findByUserAndStatus(user, Coupon.Status.AVAILABLE)
                .stream()
                .map(CouponMapper::toCouponCommonDto)
                .toList();

        reservationUserDto = UserMapper.toReservationUserDto(user, couponCommonDtos);
        return ReservationPopupResponse.from(schedule, remainHeadCount, reservationUserDto, reservationShipDto);
    }

    @Transactional
    public ReservationCreateResponse createReservation(Long userId, String grade, String publicId, ReservationPopupRequest reservationPopupRequest) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User.Grade userGrade = parseUserGrade(grade);
        /**
         * user 불러오기 (수정 가능성 o)
         */
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();
        int headCount = reservationPopupRequest.getHeadCount();
        int totalPrice = ship.getPrice() * headCount;
        String userRequest = reservationPopupRequest.getRequest();

        //비회원이라면
        if (userGrade == User.Grade.GUEST) {
            String username = reservationPopupRequest.getUsername();
            String nickname = reservationPopupRequest.getNickname();
            String phone = reservationPopupRequest.getPhone();
            //게스트 업데이트
            user.updateGuestInfo(username, nickname, phone);
        }

        Coupon coupon = null;
        if (reservationPopupRequest.getCouponId() != null) {
            coupon = couponRepository.findById(reservationPopupRequest.getCouponId())
                    .orElseThrow(CouponNotFound::new);

            if (!coupon.getUser().equals(user)) {
                throw new UnauthorizedCouponAccess();
            }
            coupon.use();
        }

        Reservation reservation = Reservation.create(headCount,userRequest,totalPrice, Reservation.Process.RESERVE_COMPLETED,user,schedule,coupon);
        reservationRepository.save(reservation);
        schedule.decreaseCurrentHeadCount(headCount);

        ReservationCreateResponse reservationCreateResponse = ReservationMapper.toReservationCreateResponse(reservation);

        return reservationCreateResponse;
    }

    private User.Grade parseUserGrade(String grade) {
        try {
            return User.Grade.valueOf(grade.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidUserGrade();
        }
    }
}
