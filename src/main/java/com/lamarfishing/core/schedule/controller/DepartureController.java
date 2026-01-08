package com.lamarfishing.core.schedule.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.schedule.dto.command.DepartureCommand;
import com.lamarfishing.core.schedule.dto.request.DepartureRequest;
import com.lamarfishing.core.schedule.dto.response.DepartureResponse;
import com.lamarfishing.core.schedule.dto.result.DepartureResult;
import com.lamarfishing.core.schedule.service.command.DepartureCommandService;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class
DepartureController {

    private final DepartureCommandService departureCommandService;
    private final UserService userService;
    /**
     *  출항 확정 메시지 전송
     */

    //더미 컨트롤러
    @PostMapping("/{schedulePublicId}/departure/confirmation")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureConfirm(@PathVariable("schedulePublicId") String publicId,
                                                                           @RequestBody DepartureRequest request){
        DepartureCommand command = DepartureCommand.from(request);
        DepartureResult result = departureCommandService.confirmation(publicId, command);

        return ResponseEntity.ok(ApiResponse.success("출항 확정 메시지를 보냈습니다.",DepartureResponse.from(result)));
    }

    /**
     * 출항 취소
     */

    @PostMapping("/{schedulePublicId}/departure/cancel")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureCancel(@PathVariable("schedulePublicId") String publicId,
                                                                          @RequestBody DepartureRequest request){
        DepartureCommand command = DepartureCommand.from(request);
        DepartureResult result = departureCommandService.cancel(publicId, command);

        return ResponseEntity.ok(ApiResponse.success("출항 취소 메시지를 보냈습니다.",DepartureResponse.from(result)));
    }

    /**
     * 출항 연기
     */

    @PostMapping("/{schedulePublicId}/departure/delay")
    public ResponseEntity<ApiResponse<DepartureResponse>> departureDelay(@PathVariable("schedulePublicId") String publicId,
                                                                         @RequestBody DepartureRequest request){
        DepartureCommand command = DepartureCommand.from(request);
        DepartureResult result = departureCommandService.delay(publicId, command);


        return ResponseEntity.ok(ApiResponse.success("출항 보류 메시지를 보냈습니다.",DepartureResponse.from(result)));
    }

}
