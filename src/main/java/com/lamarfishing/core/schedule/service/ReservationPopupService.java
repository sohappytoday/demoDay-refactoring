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
import com.lamarfishing.core.schedule.dto.request.EarlyReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.exception.UnauthorizedPopupAccess;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ReservationShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.ReservationUserDto;
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

    /**
     * 선예약 팝업 조회
     */
    public EarlyReservationPopupResponse getReservationPopup(Long userId, String publicId) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if (user.getGrade() == User.Grade.GUEST){
            throw new UnauthorizedPopupAccess();
        }

    }

    @Transactional
    public ReservationCreateResponse createReservation(Long userId, String publicId, EarlyReservationPopupRequest earlyReservationPopupRequest) {
        if (!publicId.startsWith("sch")) {
            throw new InvalidSchedulePublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        User.Grade userGrade = user.getGrade();

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        Ship ship = schedule.getShip();
        int headCount = earlyReservationPopupRequest.getHeadCount();
        int totalPrice = ship.getPrice() * headCount;
        String userRequest = earlyReservationPopupRequest.getRequest();

        //비회원이라면
        if (userGrade == User.Grade.GUEST) {
            String username = earlyReservationPopupRequest.getUsername();
            String nickname = earlyReservationPopupRequest.getNickname();
            String phone = earlyReservationPopupRequest.getPhone();
            //게스트 업데이트
            user.updateGuestInfo(username, nickname, phone);
        }

        Coupon coupon = null;
        if (earlyReservationPopupRequest.getCouponId() != null) {
            coupon = couponRepository.findById(earlyReservationPopupRequest.getCouponId())
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
}
