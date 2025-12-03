package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.domain.Status;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import com.lamarfishing.core.schedule.dto.response.DepartureResponse;
import com.lamarfishing.core.schedule.service.DepartureService;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class DepartureController {

    private final DepartureService departureService;
    private final UserService userService;
    /**
     *  출항 확정 메시지 전송
     */

    //더미 컨트롤러
    @PostMapping("/{schedulePublicId}/departure/confirmation")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureConfirm(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                           @PathVariable("schedulePublicId") String publicId,
                                                                           @RequestBody DepartureRequest request){
        Status status = request.getScheduleStatus();
        DepartureResponse response = departureService.confirmation(publicId, status);

        return ResponseEntity.ok(ApiResponse.success("출항 확정 메시지를 보냈습니다.",response));
    }

    /**
     * 출항 취소
     */

    @PostMapping("/{schedulePublicId}/departure/cancel")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureCancel(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                          @PathVariable("schedulePublicId") String publicId,
                                                                          @RequestBody DepartureRequest request){
        Status status = request.getScheduleStatus();
        DepartureResponse response = departureService.cancel(publicId, status);

        return ResponseEntity.ok(ApiResponse.success("출항 취소 메시지를 보냈습니다.",response));
    }

    /**
     * 출항 연기
     */

    @PostMapping("/{schedulePublicId}/departure/delay")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureDelay(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                         @PathVariable("schedulePublicId") String publicId,
                                                                         @RequestBody DepartureRequest request){
        Status status = request.getScheduleStatus();
        DepartureResponse response = departureService.delay(publicId, status);

        return ResponseEntity.ok(ApiResponse.success("출항 보류 메시지를 보냈습니다.",response));
    }

}
