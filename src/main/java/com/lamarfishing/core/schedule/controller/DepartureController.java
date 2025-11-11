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

    /**
     *  출항 확정 메시지 전송
     */
//    @PostMapping("/{schedulePublicId}/departure/confirmation")
//    public ResponseEntity<ApiResponse<DepartureResponse>> departureConfirm(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                           @PathVariable("schedulePublicId") String publicId,
//                                                                           @RequestBody DepartureRequest request){
//        DepartureResponse response = departureService.confirmation(userId, publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 확정 메시지를 보냈습니다.",response));
//    }

    //더미 컨트롤러
    @PostMapping("/{schedulePublicId}/departure/confirmation")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureConfirm(@PathVariable("schedulePublicId") String publicId,
                                                                           @RequestBody DepartureRequest request){
        Long userId = 2L;   //관리자
        DepartureResponse response = departureService.confirmation(userId, publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 확정 메시지를 보냈습니다.",response));
    }

    /**
     * 출항 취소
     */
//    @PostMapping("/{schedulePublicId}/departure/cancel")
//    public ResponseEntity<ApiResponse<DepartureResponse>> departureCancel(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                          @PathVariable("schedulePublicId") String publicId,
//                                                                          @RequestBody DepartureRequest request){
//        DepartureResponse response = departureService.cancel(userId, publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 취소 메시지를 보냈습니다.",response));
//    }

    @PostMapping("/{schedulePublicId}/departure/cancel")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureCancel(@PathVariable("schedulePublicId") String publicId,
                                                                          @RequestBody DepartureRequest request){
        Long userId = 2L;
        DepartureResponse response = departureService.cancel(userId, publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 취소 메시지를 보냈습니다.",response));
    }

    /**
     * 출항 연기
     */
//    @PostMapping("/{schedulePublicId}/departure/delay")
//    public ResponseEntity<ApiResponse<DepartureResponse>> departureDelay(@RequestAttribute(name = "수정필요1") Long userId,
//                                                                         @PathVariable("schedulePublicId") String publicId,
//                                                                         @RequestBody DepartureRequest request){
//        DepartureResponse response = departureService.cancel(userId, publicId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("출항 보류 메시지를 보냈습니다.",response));
//    }

    @PostMapping("/{schedulePublicId}/departure/delay")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureDelay(@PathVariable("schedulePublicId") String publicId,
                                                                         @RequestBody DepartureRequest request){
        Long userId = 2L;
        DepartureResponse response = departureService.delay(userId, publicId, request);

        return ResponseEntity.ok(ApiResponse.success("출항 보류 메시지를 보냈습니다.",response));
    }

}
