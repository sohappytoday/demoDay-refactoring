package com.lamarfishing.core.reservation.service.query;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.common.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.query.ReservationDetailFlat;
import com.lamarfishing.core.reservation.dto.result.ReservationDetailResult;
import com.lamarfishing.core.reservation.exception.ReservationNotFound;
import com.lamarfishing.core.reservation.mapper.ReservationMapper;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.reservation.dto.common.ReservationSimpleDto;
import com.lamarfishing.core.reservation.resolver.ReservationResolver;
import com.lamarfishing.core.reservation.service.policy.ReservationAccessPolicy;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.common.ReservationDetailScheduleDto;
import com.lamarfishing.core.schedule.mapper.ScheduleMapper;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.result.ReservationDetailShipDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.common.validate.ValidatePublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.lamarfishing.core.reservation.domain.Reservation.Process;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryService {

    private final ReservationRepository reservationRepository;
    private final ReservationResolver reservationResolver;
    private final ReservationAccessPolicy reservationAccessPolicy;

    @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_BASIC', 'GRADE_VIP')")
    public Page<ReservationSimpleDto> getMyReservations(Long userId, Process process, Pageable pageable) {
        return reservationRepository.getReservations(userId, process, null, null, null, pageable);
    }

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<ReservationSimpleDto> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable) {
        return reservationRepository.getReservations(null, process, from, to, shipId, pageable);
    }

    // @PreAuthorize("hasAnyAuthority('GRADE_ADMIN','GRADE_BAISC','GRADE_VIP')")
    public ReservationDetailResult getReservationDetail(User user, String publicId) {

        ValidatePublicId.validateReservationPublicId(publicId);

        //redis 사용시 치환
        Long reservationId = reservationResolver.resolve(publicId);

        // 내 reservation인지 확인
        reservationAccessPolicy.validateOwnerOrAdmin(reservationId, user.getId());

        ReservationDetailFlat flatDto = reservationRepository.getReservationDetail(reservationId);
        ReservationDetailShipDto shipDto = ReservationDetailShipDto.from(flatDto);
        ReservationDetailDto detailDto = ReservationDetailDto.from(flatDto);
        ReservationDetailScheduleDto scheduleDto = ReservationDetailScheduleDto.from(flatDto);

        return ReservationDetailResult.of(shipDto, detailDto, scheduleDto);
    }

    private Reservation findReservation(String pubilcId){
        Reservation reservation = reservationRepository.findByPublicId(pubilcId).orElseThrow(ReservationNotFound::new);
        return reservation;
    }
}
