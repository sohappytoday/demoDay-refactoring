package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import com.lamarfishing.core.schedule.dto.response.DepartureResponse;
import com.lamarfishing.core.schedule.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class DepartureController {

    private final DepartureService departureService;

    @PostMapping("/{schedule_public_id}/departure/confirmation")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureConfirm(@PathVariable("schedule_public_id") String publicId,
                                                                                @RequestBody DepartureRequest request){
        DepartureResponse response = departureService.confirmation(publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 확정 메시지를 보냈습니다.",response));
    }

    @PostMapping("/{schedule_public_id}/departure/cancel")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureCancel(@PathVariable("schedule_public_id") String publicId,
                                                                                @RequestBody DepartureRequest request){
        DepartureResponse response = departureService.cancel(publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 취소 메시지를 보냈습니다.",response));
    }

    @PostMapping("/{schedule_public_id}/departure/delay")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureDelay(@PathVariable("schedule_public_id") String publicId,
                                                                          @RequestBody DepartureRequest request){
        DepartureResponse response = departureService.cancel(publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 보류 메시지를 보냈습니다.",response));
    }

}
