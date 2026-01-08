package com.lamarfishing.core.schedule.service.query;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.query.CouponCommonDto;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.service.command.ReservationCommandService;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.query.EarlyReservationPopupFlatDto;
import com.lamarfishing.core.schedule.dto.result.EarlyReservationPopupResult;
import com.lamarfishing.core.schedule.dto.result.NormalReservationPopupResult;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.exception.UnauthorizedPopupAccess;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.schedule.resolver.ScheduleResolver;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.query.EarlyReservationUserDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import com.lamarfishing.core.user.mapper.UserMapper;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

/**
 * parameter userId만 받게 fix 필요
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationPopupQueryService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ScheduleResolver scheduleResolver;

    /**
     * 선예약 팝업 조회
     */
    // @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public EarlyReservationPopupResult getEarlyReservationPopup(User user, String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);
        Long scheduleId = scheduleResolver.resolve(publicId);

        EarlyReservationPopupFlatDto flatDto = scheduleRepository.getScheduleAndShipPopup(scheduleId);
        if(flatDto.getScheduleType() != Type.EARLY){
            throw new UnauthorizedPopupAccess();
        }
        ReservationShipDto shipDto = ReservationShipDto.from(flatDto);

        List<CouponCommonDto> coupons = couponRepository.findAvailableByUserId(user.getId());
        EarlyReservationUserDto userDto = EarlyReservationUserDto.builder()
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .grade(user.getGrade())
                        .phone(user.getPhone())
                        .coupons(coupons)
                        .build();

        return EarlyReservationPopupResult.of(flatDto, shipDto, userDto);

    }

    /**
     * 일반예약 팝업 조회 (회원, 관리자)
     */
    // @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public NormalReservationPopupResult getNormalReservationPopupUser(User user, String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.NORMAL){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto(user);
        return NormalReservationPopupResult.of(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);

    }

    /**
     * 비회원 예약 팝업 조회
     */
   public NormalReservationPopupResult getNormalReservationPopupGuest(String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.NORMAL){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        NormalReservationUserDto normalReservationUserDto = UserMapper.toNormalReservationUserDto();

        return NormalReservationPopupResult.of(schedule,remainHeadCount, normalReservationUserDto,reservationShipDto);

    }

}
