package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.dto.command.ScheduleCreateCommand;
import com.lamarfishing.core.schedule.dto.command.UpdateDepartureTimeCommand;
import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.request.UpdateDepartureTimeRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.dto.response.ScheduleMainResponse;
import com.lamarfishing.core.schedule.dto.response.DepartureTimeResponse;
import com.lamarfishing.core.schedule.dto.result.ScheduleDetailResult;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;
import com.lamarfishing.core.schedule.service.command.ScheduleCommandService;
import com.lamarfishing.core.schedule.service.query.ScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleQueryService scheduleQueryService;
    private final ScheduleCommandService scheduleCommandService;

    /**
     * 출항 일정 상세보기
     */
    @GetMapping("/{schedulePublicId}")
    public ResponseEntity<ApiResponse<ScheduleDetailResponse>> getScheduleDetail(@PathVariable("schedulePublicId") String publicId) {
        ScheduleDetailResult result = scheduleQueryService.getScheduleDetail(publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 상세보기에 성공하였습니다.", ScheduleDetailResponse.from(result)));
    }

    /**
     * 출항 일정 생성하기
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSchedule(@RequestBody ScheduleCreateRequest req) {

        scheduleCommandService.createSchedule(ScheduleCreateCommand.from(req));

        return ResponseEntity.ok(ApiResponse.success("출항 일정 생성에 성공하였습니다."));

    }

    /**
     * 출항 일정 삭제
     */
    @DeleteMapping("/{schedulePublicId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable("schedulePublicId") String publicId) {

        scheduleCommandService.deleteSchedule(publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 삭제에 성공하였습니다.",null));
    }

    /**
     * 관리자 : 메인페이지서 출항 시간 보기
     */
    @GetMapping("/departure")
    public ResponseEntity<ApiResponse<DepartureTimeResponse>> getDepartureTime() {

        DepartureTimeResult result = scheduleQueryService.viewDepartureTime();

        return ResponseEntity.ok(ApiResponse.success("출항 시간 조회에 성공하였습니다.", DepartureTimeResponse.from(result)));
    }

    /**
     * 출항 시간 변경 api
     * */
    @PatchMapping("/{schedulePublicId}/departure")
    public ResponseEntity<ApiResponse<Void>> updateDepartureTime(@PathVariable String schedulePublicId,
                                                                 @RequestBody UpdateDepartureTimeRequest req){

        scheduleCommandService.updateDepartureTime(schedulePublicId, UpdateDepartureTimeCommand.from(req));

        return ResponseEntity.ok(ApiResponse.success("출항 시간 수정에 성공하였습니다.", null));
    }

    @GetMapping("/main")
    public ResponseEntity<ApiResponse<ScheduleMainResponse>> getSchedules(LocalDateTime from, LocalDateTime to) {
        List<ScheduleMainDto> schedules = scheduleQueryService.getSchedules(from, to);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 목록 조회에 성공하였습니다.", ScheduleMainResponse.from(schedules)));
    }
}
