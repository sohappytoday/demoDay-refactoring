package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.NormalReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.service.ReservationPopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{schedulePublicId}/reservation")
public class ReservationPopupController {

    private final ReservationPopupService reservationPopupService;
    /**
     * 선예약 팝업 조회
     */
//    @GetMapping("/early")
//    public ResponseEntity<ApiResponse<EarlyReservationPopupResponse>> getEarlyReservationPopup(@RequestHeader Long userId,
//                                                                                    @PathVariable("schedulePublicId") String publicId){
//
//        EarlyReservationPopupResponse response = reservationPopupService.getEarlyReservationPopup(userId,publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("예약 팝업 조회에 성공하였습니다",response));
//    }

    @GetMapping("/early")
    public ResponseEntity<ApiResponse<EarlyReservationPopupResponse>> getEarlyReservationPopup(@PathVariable("schedulePublicId") String publicId){

        Long userId = 1L;
        EarlyReservationPopupResponse response = reservationPopupService.getEarlyReservationPopup(userId,publicId);

        return ResponseEntity.ok(ApiResponse.success("선예약 팝업 조회에 성공하였습니다",response));
    }

    /**
     * 일반예약 팝업 조회
     */
//    @GetMapping("/normal")
//    public ResponseEntity<ApiResponse<NormalReservationPopupResponse>> getNormalReservationPopup(@RequestHeader Long userId, @PathVariable("schedulePublicId") String publicId){
//
//        NormalReservationPopupResponse response = reservationPopupService.getNormalReservationPopup(userId,publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("선예약 팝업 조회에 성공하였습니다",response));
//    }

    @GetMapping("/normal")
    public ResponseEntity<ApiResponse<NormalReservationPopupResponse>> getNormalReservationPopup(@PathVariable("schedulePublicId") String publicId){

        Long userId = 1L;
        NormalReservationPopupResponse response = reservationPopupService.getNormalReservationPopup(userId,publicId);

        return ResponseEntity.ok(ApiResponse.success("일반예약 팝업 조회에 성공하였습니다",response));
    }

    /**
     * 예약
     */
//    @PostMapping
//    public ResponseEntity<ApiResponse<ReservationCreateResponse>> createReservation(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                                    @PathVariable("schedule_public_id") String publicId,
//                                                                                    @RequestBody ReservationPopupRequest reservationPopupRequest) {
//
//        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(userId, publicId, reservationPopupRequest);
//
//        return ResponseEntity.ok(ApiResponse.success("예약을 성공하였습니다.",reservationCreateResponse));
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationCreateResponse>> createReservation(@PathVariable("schedulePublicId") String publicId,
                                                                                    @RequestBody ReservationPopupRequest req) {
        Long userId = 3L; //VIP

        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(
                userId, publicId,
                req.getUsername(), req.getNickname(), req.getPhone(), req.getHeadCount(), req.getRequest(), req.getCouponId());

        return ResponseEntity.ok(ApiResponse.success("예약을 성공하였습니다.",reservationCreateResponse));
    }
}
