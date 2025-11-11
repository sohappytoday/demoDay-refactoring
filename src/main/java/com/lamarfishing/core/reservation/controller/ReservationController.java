package com.lamarfishing.core.reservation.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.coupon.service.CouponService;
import com.lamarfishing.core.reservation.dto.request.ReservationProcessUpdateRequest;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
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
     * 입금 확인/취소 접수/취소 완료 변경
     * User.Grade == ADMIN -> 입금확인/취소 완료만 가능
     * User.Grade != ADMIN -> 취소 접수만 가능
     */
//    @PatchMapping("/{reservationPublicId}/process")
//    public ResponseEntity<ApiResponse<Void>> changeReservationProcess(@RequestHeader Long userId,
//                                                           @PathVariable("reservationPublicId") String publicId,
//                                                           @RequestBody ReservationProcessUpdateRequest request) {
//        reservationService.changeReservationProcess(userId, publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("예약 상태 변경에 성공하였습니다."));
//    }
    @PatchMapping("/{reservationPublicId}/process")
    public ResponseEntity<ApiResponse<Void>> changeReservationProcess(@PathVariable("reservationPublicId") String publicId,
                                                                      @RequestBody ReservationProcessUpdateRequest request) {
        //예약 취소는 당장은 불가능 프론트가 필요시 1L로 바꿀것
        Long userId = 2L;
        reservationService.changeReservationProcess(userId, publicId, request);

        return ResponseEntity.ok(ApiResponse.success("예약 상태 변경에 성공하였습니다."));
    }
}

