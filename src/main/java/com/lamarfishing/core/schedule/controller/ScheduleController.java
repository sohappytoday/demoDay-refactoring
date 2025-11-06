package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{schedule_public_id}")
    public ResponseEntity<ApiResponse<ScheduleDetailResponse>> getScheduleDetail(@PathVariable String publicId) {
        ScheduleDetailResponse scheduleDetailResponse = scheduleService.getScheduleDetail(publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 상세보기에 성공하였습니다.", scheduleDetailResponse));
    }
}
