package com.lamarfishing.core.reservation.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.coupon.service.CouponService;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import com.lamarfishing.core.reservation.dto.request.ReservationProcessUpdateRequest;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.service.ReservationQueryService;
import com.lamarfishing.core.reservation.service.ReservationService;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationQueryService reservationQueryService;
    private final ReservationService reservationService;
    private final CouponService couponService;
    private final UserService userService;

    /**
     * 예약 상세 조회
     */
    //더미 컨트롤러
    @GetMapping("/{reservationPublicId}")
    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                       @PathVariable("reservationPublicId") String publicId){
        Long userId = userService.findUserId(authenticatedUser);
        ReservationDetailResponse reservationDetailResponse = reservationService.getReservationDetail(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회에 성공하였습니다.",reservationDetailResponse));
    }

    /**
     * 쿠폰 발급
     */
    @PostMapping("/{reservationPublicId}/coupon")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                         @PathVariable("reservationPublicId") String publicId) {

        Long userId = userService.findUserId(authenticatedUser);
        couponService.issueCoupon(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("쿠폰을 발급하였습니다.",null));
    }

    /**
     * 예약자 취소 예약 신청
     */
    @PatchMapping("/{reservationPublicId}/cancel-request")
    public ResponseEntity<ApiResponse<Void>> reservationCancelRequest(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                      @PathVariable("reservationPublicId") String publicId,
                                                                      @RequestBody ReservationProcessUpdateRequest request) {
        Long userId = userService.findUserId(authenticatedUser);
        Process process = request.getProcess();
        reservationService.reservationCancelRequest(userId, publicId, process);

        return ResponseEntity.ok(ApiResponse.success("예약 취소에 성공하였습니다."));
    }

    /**
     * 관리자 예약 상태 변경
     */
    @PatchMapping("/{reservationPublicId}/process")
    public ResponseEntity<ApiResponse<Void>> changeReservationProcess(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                      @PathVariable("reservationPublicId") String publicId,
                                                                      @RequestBody ReservationProcessUpdateRequest request) {
        Long userId = userService.findUserId(authenticatedUser);
        Reservation.Process process = request.getProcess();
        reservationService.changeReservationProcess(userId, publicId, process);

        return ResponseEntity.ok(ApiResponse.success("예약 취소에 성공하였습니다."));
    }
    /**
     * 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getReservations(Process process, LocalDateTime from, LocalDateTime to, Long shipId, Pageable pageable) {

        Page<ReservationSimpleDto> pageResult = reservationQueryService.getReservations(process, from, to, shipId, pageable);

        return ResponseEntity.ok(ApiResponse.success("예약 목록 조회에 성공하였습니다.", PageResponse.from(pageResult)));
    }
}

