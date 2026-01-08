package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.dto.command.ReservationPopupCommand;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.EarlyReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.NormalReservationPopupResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.dto.result.EarlyReservationPopupResult;
import com.lamarfishing.core.schedule.dto.result.NormalReservationPopupResult;
import com.lamarfishing.core.schedule.dto.result.ReservationCreateResult;
import com.lamarfishing.core.schedule.service.command.ReservationPopupCommandService;
import com.lamarfishing.core.schedule.service.query.ReservationPopupQueryService;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{schedulePublicId}/reservation")
public class ReservationPopupController {

    private final ReservationPopupQueryService reservationPopupQueryService;
    private final UserService userService;
    private final ReservationPopupCommandService reservationPopupCommandService;

    /**
     * 선예약 팝업 조회
     */
    @GetMapping("/early")
    public ResponseEntity<ApiResponse<EarlyReservationPopupResponse>> getEarlyReservationPopup(// @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                               @PathVariable("schedulePublicId") String publicId){

        // User user = userService.findUser(authenticatedUser);
        User user = userService.findBasicUser();
        EarlyReservationPopupResult result = reservationPopupQueryService.getEarlyReservationPopup(user,publicId);

        return ResponseEntity.ok(ApiResponse.success("선예약 팝업 조회에 성공하였습니다",EarlyReservationPopupResponse.from(result)));
    }

    /**
     * 일반예약 팝업 조회
     */
    @GetMapping("/normal")
    public ResponseEntity<ApiResponse<NormalReservationPopupResponse>> getNormalReservationPopup(// @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                                 @PathVariable("schedulePublicId") String publicId){
//        User user = userService.findGuestUser();
//        NormalReservationPopupResult result = reservationPopupQueryService.getNormalReservationPopupGuest(publicId);
//        return ResponseEntity.ok(ApiResponse.success("일반예약 팝업 조회에 성공하였습니다",NormalReservationPopupResponse.from(result)));

        User user = userService.findBasicUser();
        NormalReservationPopupResult result = reservationPopupQueryService.getNormalReservationPopupUser(user,publicId);
        return ResponseEntity.ok(ApiResponse.success("일반예약 팝업 조회에 성공하였습니다",NormalReservationPopupResponse.from(result)));
    }

    /**
     * 예약 생성하기
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationCreateResponse>> createReservation(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                    @PathVariable("schedulePublicId") String publicId,
                                                                                    @RequestBody ReservationPopupRequest req) {
        if(authenticatedUser == null){
            ReservationCreateResult result = reservationPopupCommandService.createReservationGuest(publicId, ReservationPopupCommand.from(req));

            return ResponseEntity.ok(ApiResponse.success("비회원 예약을 성공하였습니다.", ReservationCreateResponse.from(result)));
        }

        User user = userService.findUser(authenticatedUser);
        ReservationCreateResult result = reservationPopupCommandService.createReservationUser(user, publicId, ReservationPopupCommand.from(req));
        return ResponseEntity.ok(ApiResponse.success("회원 예약을 성공하였습니다.", ReservationCreateResponse.from(result)));
    }
}
