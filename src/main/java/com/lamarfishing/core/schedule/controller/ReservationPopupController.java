package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationPopupResponse;
import com.lamarfishing.core.schedule.service.ReservationPopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{schedule_public_id}/reservation")
public class ReservationPopupController {

    private final ReservationPopupService reservationPopupService;
    /**
     * 토큰 받는 방식 수정 필요
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ReservationPopupResponse>> getReservationPopup(@RequestAttribute(name = "수정필요1") Long userId,
                                                                                    @RequestAttribute(name = "수정필요2") String grade,
                                                                                    @PathVariable("schedule_public_id") String publicId){

        ReservationPopupResponse popupResponse = reservationPopupService.getReservationPopup(userId,grade,publicId);

        return ResponseEntity.ok(ApiResponse.success("예약 팝업 조회에 성공하였습니다",popupResponse));
    }

}
