package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.NormalReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.service.ReservationPopupService;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.dto.command.NormalReservationUserDto;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{schedulePublicId}/reservation")
public class ReservationPopupController {

    private final ReservationPopupService reservationPopupService;
    private final UserService userService;
    /**
     * 선예약 팝업 조회
     */
    @GetMapping("/early")
    public ResponseEntity<ApiResponse<EarlyReservationPopupResponse>> getEarlyReservationPopup(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                               @PathVariable("schedulePublicId") String publicId){

        Long userId = userService.findUserId(authenticatedUser);
        EarlyReservationPopupResponse response = reservationPopupService.getEarlyReservationPopup(userId,publicId);

        return ResponseEntity.ok(ApiResponse.success("선예약 팝업 조회에 성공하였습니다",response));
    }

    /**
     * 일반예약 팝업 조회
     */
    @GetMapping("/normal")
    public ResponseEntity<ApiResponse<NormalReservationPopupResponse>> getNormalReservationPopup(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                                 @PathVariable("schedulePublicId") String publicId){

        if(authenticatedUser==null){
            NormalReservationPopupResponse response = reservationPopupService.getNormalReservationPopupGuest(publicId);
            return ResponseEntity.ok(ApiResponse.success("일반예약 팝업 조회에 성공하였습니다",response));
        }
        Long userId = userService.findUserId(authenticatedUser);
        NormalReservationPopupResponse response = reservationPopupService.getNormalReservationPopupUser(userId,publicId);
        return ResponseEntity.ok(ApiResponse.success("일반예약 팝업 조회에 성공하였습니다",response));
    }

    /**
     * 예약 생성하기
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationCreateResponse>> createReservation(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                    @PathVariable("schedulePublicId") String publicId,
                                                                                    @RequestBody ReservationPopupRequest req) {
        if(authenticatedUser == null){
            ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservationGuest(publicId,
                    req.getUsername(), req.getNickname(), req.getPhone(), req.getHeadCount(), req.getRequest(), req.getCouponId());

            return ResponseEntity.ok(ApiResponse.success("예약을 성공하였습니다.",reservationCreateResponse));
        }

        Long userId = userService.findUserId(authenticatedUser);
        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservationUser(userId, publicId,
                req.getUsername(), req.getNickname(), req.getPhone(), req.getHeadCount(), req.getRequest(), req.getCouponId());
        return ResponseEntity.ok(ApiResponse.success("예약을 성공하였습니다.",reservationCreateResponse));
    }
}
