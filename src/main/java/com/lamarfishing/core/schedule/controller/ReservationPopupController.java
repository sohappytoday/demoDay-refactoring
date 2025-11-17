package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.dto.request.EarlyReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
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
//    @GetMapping
//    public ResponseEntity<ApiResponse<ReservationPopupResponse>> getReservationPopup(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                                    @RequestAttribute(name = "수정필요2") String grade,
//                                                                                    @PathVariable("schedule_public_id") String publicId){
//
//        ReservationPopupResponse popupResponse = reservationPopupService.getReservationPopup(userId,grade,publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("예약 팝업 조회에 성공하였습니다",popupResponse));
//    }

    //더미 컨트롤러
    @GetMapping("/early")
    public ResponseEntity<ApiResponse<EarlyReservationPopupResponse>> getReservationPopup(@PathVariable("schedulePublicId") String publicId){

        Long userId = 1L;   //BASIC
        EarlyReservationPopupResponse popupResponse = reservationPopupService.getReservationPopup(userId,publicId);

        return ResponseEntity.ok(ApiResponse.success("선예약 팝업 조회에 성공하였습니다",popupResponse));
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
                                                                                    @RequestBody EarlyReservationPopupRequest reservationPopupRequest) {

        Long userId = 1L; //BASIC
        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(userId, publicId, reservationPopupRequest);

        return ResponseEntity.ok(ApiResponse.success("예약을 성공하였습니다.",reservationCreateResponse));
    }
}
