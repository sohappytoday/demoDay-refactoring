package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

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
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> createSchedule(@RequestHeader Long userId,
//                                                            @RequestBody ScheduleCreateRequest scheduleCreateRequest) {
//        scheduleService.createSchedule(userId,scheduleCreateRequest);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 일정 생성에 성공하였습니다."));
//
//    }
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSchedule(@RequestBody ScheduleCreateRequest scheduleCreateRequest) {
        Long userId = 2L;
        scheduleService.createSchedule(userId,scheduleCreateRequest);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 생성에 성공하였습니다."));

    }

    /**
     * 출항 일정 삭제
     */
//    @DeleteExchange("/{schedulePublicId}")
//    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@RequestHeader Long userId,
//                                                            @PathVariable("schedulePublicId") String publicId) {
//        scheduleService.deleteSchedule(userId,publicId);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 일정 삭제에 성공하였습니다."));
//    }
    @DeleteExchange("/{schedulePublicId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable("schedulePublicId") String publicId) {
        Long userId = 2L;
        scheduleService.deleteSchedule(userId,publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 삭제에 성공하였습니다."));
    }
}
