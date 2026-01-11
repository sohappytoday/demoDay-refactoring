package com.lamarfishing.core.reservation.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.coupon.service.command.CouponCommandService;
import com.lamarfishing.core.reservation.dto.command.ReservationProcessUpdateCommand;
import com.lamarfishing.core.reservation.dto.common.ReservationSimpleDto;
import com.lamarfishing.core.reservation.dto.request.ReservationProcessUpdateRequest;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.dto.result.ReservationDetailResult;
import com.lamarfishing.core.reservation.service.query.ReservationQueryService;
import com.lamarfishing.core.reservation.service.command.ReservationCommandService;
import com.lamarfishing.core.user.domain.User;
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
    private final ReservationCommandService reservationCommandService;
    private final CouponCommandService couponCommandService;
    private final UserService userService;

    /**
     * 예약 상세 조회
     */
    //더미 컨트롤러
    @GetMapping("/{reservationPublicId}")
    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(// @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                       @PathVariable("reservationPublicId") String publicId){
        // User user = userService.findUser(authenticatedUser);
        User user = userService.findBasicUser();
        ReservationDetailResult result = reservationQueryService.getReservationDetail(user, publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회에 성공하였습니다.", ReservationDetailResponse.from(result)));
    }

    /**
     * 쿠폰 발급
     */
    @PostMapping("/{reservationPublicId}/coupon")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(// @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                         @PathVariable("reservationPublicId") String publicId) {

        couponCommandService.issueCoupon(publicId);

        return ResponseEntity.ok(ApiResponse.success("쿠폰을 발급하였습니다.",null));
    }

    /**
     * 예약자 취소 예약 신청
     */
    @PostMapping("/{reservationPublicId}/cancel/request")
    public ResponseEntity<ApiResponse<Void>> reservationRequestCancel(@PathVariable("reservationPublicId") String publicId) {
        reservationCommandService.cancelRequest(publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 취소에 성공하였습니다.", null));
    }

    /**
     * 관리자 - 예약자 입금 완료
     */
    @PostMapping("/{reservationPublicId}/deposit/complete")
    public ResponseEntity<ApiResponse<Void>> reservationCompleteDeposit(@PathVariable("reservationPublicId") String publicId) {

        reservationCommandService.completeDeposit(publicId);

        return ResponseEntity.ok(ApiResponse.success("입금 완료로 변경하였습니다.", null));
    }

    /**
     * 관리자 - 예약자 취소 완료
     */
    @PostMapping("/{reservationPublicId}/cancel/complete")
    public ResponseEntity<ApiResponse<Void>> reservationCompleteCancel(@PathVariable("reservationPublicId") String publicId) {

        reservationCommandService.completeCancel(publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 취소로 변경하였습니다.", null));
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

