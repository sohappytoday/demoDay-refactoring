package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.schedule.dto.request.ScheduleCreateRequest;
import com.lamarfishing.core.schedule.dto.request.UpdateDepartureTimeRequest;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.dto.response.ViewDepartureTimeResponse;
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
        scheduleService.createSchedule(userId, scheduleCreateRequest);

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
        scheduleService.deleteSchedule(userId, publicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 삭제에 성공하였습니다."));
    }

    /**
     * 관리자 : 메인페이지서 출항 시간 보기
     */
//    @GetMapping("/departure-time")
//    public ResponseEntity<ApiResponse<ViewDepartureTimeResponse>> getDepartureTime(@RequestHeader Long userId) {
//        ViewDepartureTimeResponse viewDepartureTimeResponse =scheduleService.viewDepartureTime(userId);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 시간 조회에 성공하였습니다.",viewDepartureTimeResponse));
//    }
    @GetMapping("/departure")
    public ResponseEntity<ApiResponse<ViewDepartureTimeResponse>> getDepartureTime() {
        Long userId = 2L;
        ViewDepartureTimeResponse viewDepartureTimeResponse = scheduleService.viewDepartureTime(userId);

        return ResponseEntity.ok(ApiResponse.success("출항 시간 조회에 성공하였습니다.", viewDepartureTimeResponse));
    }

    /**
     * 출항 시간 변경 api
     * */
//    @PatchMapping("/{schedulePublicId}/departure")
//    public ResponseEntity<ApiResponse<Void>> UpdateDepartureTime(@RequestHeader Long userId,
//                                                                 @PathVariable String publicId,
//                                                                 @RequestBody UpdateDepartureTimeRequest request){
//        scheduleService.UpdateDepartureTime(userId,publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 시간 수정에 성공하였습니다."));
//    }
    @PatchMapping("/{schedulePublicId}/departure")
    public ResponseEntity<ApiResponse<Void>> UpdateDepartureTime(@PathVariable String schedulePublicId,
                                                                 @RequestBody UpdateDepartureTimeRequest request){
        Long userId = 2L;
        scheduleService.UpdateDepartureTime(userId,schedulePublicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 시간 수정에 성공하였습니다."));
    }

    /**
     * 선예약 마감
     */
//    @PatchMapping("/{schedulePublicId}/drawn")
//    public ResponseEntity<ApiResponse<Void>> markAsDrawn(@RequestHeader Long userId,
//                                                         @PathVariable String schedulePublicId) {
//        scheduleService.markAsDrawn(userId, schedulePublicId);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 일정 선예약 마감이 되었습니다."));
//    }
    @PatchMapping("/{schedulePublicId}/drawn")
    public ResponseEntity<ApiResponse<Void>> markAsDrawn(@PathVariable String schedulePublicId) {
        Long userId = 2L;
        scheduleService.markAsDrawn(userId, schedulePublicId);

        return ResponseEntity.ok(ApiResponse.success("출항 일정 선예약 마감이 되었습니다."));
    }
}
