package com.lamarfishing.core.reservation.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

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
}
