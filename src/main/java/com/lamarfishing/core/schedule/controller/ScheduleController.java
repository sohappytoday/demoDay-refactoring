package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.command.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.request.UpdateDepartureTimeRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.dto.response.ScheduleMainResponse;
import com.lamarfishing.core.schedule.dto.response.ViewDepartureTimeResponse;
import com.lamarfishing.core.schedule.service.ScheduleQueryService;
import com.lamarfishing.core.schedule.service.ScheduleService;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleQueryService scheduleQueryService;
    private final ScheduleService scheduleService;
    private final UserService userService;

    /**
     * 출항 일정 상세보기
     */
    @GetMapping("/{schedulePublicId}")
    public ResponseEntity<ApiResponse<ScheduleDetailResponse>> getScheduleDetail(@PathVariable("schedulePublicId") String publicId) {
        ScheduleDetailResponse scheduleDetailResponse = scheduleService.getScheduleDetail(publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 상세보기에 성공하였습니다.", scheduleDetailResponse));
    }

    /**
     * 출항 일정 생성하기
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSchedule(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                            @RequestBody ScheduleCreateRequest req) {
        Long userId = userService.findUserId(authenticatedUser);
        scheduleService.createSchedule(userId, req.getStartDate(), req.getEndDate(), req.getShipId(), req.getScheduleType());

        return ResponseEntity.ok(ApiResponse.success("출항 일정 생성에 성공하였습니다."));

    }

    /**
     * 출항 일정 삭제
     */
    @DeleteMapping("/{schedulePublicId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                            @PathVariable("schedulePublicId") String publicId) {
        Long userId = userService.findUserId(authenticatedUser);
        scheduleService.deleteSchedule(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 삭제에 성공하였습니다.",null));
    }

    /**
     * 관리자 : 메인페이지서 출항 시간 보기
     */
    @GetMapping("/departure")
    public ResponseEntity<ApiResponse<ViewDepartureTimeResponse>> getDepartureTime(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        Long userId = userService.findUserId(authenticatedUser);
        ViewDepartureTimeResponse viewDepartureTimeResponse = scheduleService.viewDepartureTime(userId);

        return ResponseEntity.ok(ApiResponse.success("출항 시간 조회에 성공하였습니다.", viewDepartureTimeResponse));
    }

    /**
     * 출항 시간 변경 api
     * */
    @PatchMapping("/{schedulePublicId}/departure")
    public ResponseEntity<ApiResponse<Void>> updateDepartureTime(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                 @PathVariable String schedulePublicId,
                                                                 @RequestBody UpdateDepartureTimeRequest req){

        Long userId = userService.findUserId(authenticatedUser);
        scheduleService.updateDepartureTime(userId,schedulePublicId, req.getDepartureTime());

        return ResponseEntity.ok(ApiResponse.success("출항 시간 수정에 성공하였습니다."));
    }

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<ScheduleMainResponse>> getSchedules(LocalDateTime from, LocalDateTime to) {
        List<ScheduleMainDto> schedules = scheduleQueryService.getSchedules(from, to);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 목록 조회에 성공하였습니다.", ScheduleMainResponse.from(schedules)));
    }
}
