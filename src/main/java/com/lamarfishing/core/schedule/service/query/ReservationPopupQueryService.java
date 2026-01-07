package com.lamarfishing.core.schedule.service.query;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.service.command.ReservationCommandService;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.result.EarlyReservationPopupResult;
import com.lamarfishing.core.schedule.dto.result.NormalReservationPopupResult;
import com.lamarfishing.core.schedule.exception.ScheduleNotFound;
import com.lamarfishing.core.schedule.exception.UnauthorizedPopupAccess;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.dto.result.ReservationShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.EarlyReservationUserDto;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import com.lamarfishing.core.user.mapper.UserMapper;
import com.lamarfishing.core.user.repository.UserRepository;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
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

    private final StatisticService statisticService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCommandService reservationCommandService;

    /**
     * 선예약 팝업 조회
     */
    // @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_VIP','GRADE_BASIC')")
    public EarlyReservationPopupResult getEarlyReservationPopup(User user, String publicId) {

        ValidatePublicId.validateSchedulePublicId(publicId);

        Schedule schedule = scheduleRepository.findByPublicId(publicId).orElseThrow(ScheduleNotFound::new);
        if (schedule.getType() != Type.EARLY){
            throw new UnauthorizedPopupAccess();
        }

        ReservationShipDto reservationShipDto = ShipMapper.toReservationShipDto(schedule.getShip());
        List<CouponCommonDto> coupons = couponRepository.findByUserAndStatusAndType(user, Coupon.Status.AVAILABLE,getCouponTypeByDeparture(schedule.getDeparture()))
                .stream()
                .map(CouponMapper::toCouponCommonDto)
                .toList();
        EarlyReservationUserDto earlyReservationUserDto = UserMapper.toEarlyReservationUserDto(user,coupons);
        Integer remainHeadCount = schedule.getShip().getMaxHeadCount() - schedule.getCurrentHeadCount();

        return EarlyReservationPopupResult.of(schedule,remainHeadCount, earlyReservationUserDto,reservationShipDto);
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

    private Coupon.Type getCouponTypeByDeparture(LocalDateTime departure) {
        DayOfWeek day = departure.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return Coupon.Type.WEEKEND;
        }
        return Coupon.Type.WEEKDAY;
    }
}
