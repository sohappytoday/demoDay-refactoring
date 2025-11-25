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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lamarfishing.core.reservation.domain.Reservation.Process;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationQueryService reservationQueryService;
    private final ReservationService reservationService;
    private final CouponService couponService;

    /**
     * 예약 상세 조회
     */
//    @GetMapping("/{reservationPublicId}")
//    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                                       @PathVariable("reservationPublicId") String publicId){
//        ReservationDetailResponse reservationDetailResponse = reservationService.getReservationDetail(userId, publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회에 성공하였습니다.",reservationDetailResponse));
//    }

    //더미 컨트롤러
    @GetMapping("/{reservationPublicId}")
    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(@PathVariable("reservationPublicId") String publicId){
        Long userId = 1L;
        ReservationDetailResponse reservationDetailResponse = reservationService.getReservationDetail(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회에 성공하였습니다.",reservationDetailResponse));
    }

    /**
     * 쿠폰 발급
     */
//    @PostMapping("/{reservationPublicId}/coupon")
//    public ResponseEntity<ApiResponse<Void>> issueCoupon(@RequestHeader Long userId,
//                                                         @PathVariable("reservationPublicId") String publicId) {
//        couponService.issueCoupon(userId, publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("쿠폰을 발급하였습니다."));
//    }
    @PostMapping("/{reservationPublicId}/coupon")
    public ResponseEntity<ApiResponse<Void>> issueCoupon(@PathVariable("reservationPublicId") String publicId) {
        Long userId = 2L;
        couponService.issueCoupon(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("쿠폰을 발급하였습니다."));
    }

    /**
     * 예약자 취소 예약 신청
     */
//    @PatchMapping("/{reservationPublicId}/process")
//    public ResponseEntity<ApiResponse<Void>> changeReservationProcess(@RequestHeader Long userId,
//                                                           @PathVariable("reservationPublicId") String publicId,
//                                                           @RequestBody ReservationProcessUpdateRequest request) {
//        reservationService.changeReservationProcess(userId, publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("예약 상태 변경에 성공하였습니다."));
//    }
    @PatchMapping("/{reservationPublicId}/cancel-request")
    public ResponseEntity<ApiResponse<Void>> ReservationCancelRequest(@PathVariable("reservationPublicId") String publicId,
                                                                      @RequestBody ReservationProcessUpdateRequest request) {
        Long userId = 1L;
        Process process = request.getProcess();
        reservationService.ReservationCancelRequest(userId, publicId, process);

        return ResponseEntity.ok(ApiResponse.success("예약 취소에 성공하였습니다."));
    }

    /**
     * 관리자 예약 상태 변경
     */
    @PatchMapping("/{reservationPublicId}/process")
    public ResponseEntity<ApiResponse<Void>> ChangeReservationProcess(@PathVariable("reservationPublicId") String publicId,
                                                                      @RequestBody ReservationProcessUpdateRequest request) {
        Long userId = 2L;
        Reservation.Process process = request.getProcess();
        reservationService.ChangeReservationProcess(userId, publicId, process);

        return ResponseEntity.ok(ApiResponse.success("예약 취소에 성공하였습니다."));
    }
    /**
     * 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getReservations(Process process, Pageable pageable) {

        Page<ReservationSimpleDto> pageResult = reservationQueryService.getReservations(process, pageable);

        return ResponseEntity.ok(ApiResponse.success("예약 목록 조회에 성공하였습니다.", PageResponse.from(pageResult)));
    }
}

