package com.lamarfishing.core.ship.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.ship.dto.response.ShipListResponse;
import com.lamarfishing.core.ship.service.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    /**
     * 배 리스트 조회 / 스케쥴 생성할 때 필요한 조회
     */
//    @GetMapping
//    public ResponseEntity<ApiResponse<ShipListResponse>> getShips(@RequestParam Long userId){
//
//        ShipListResponse shipListResponse = shipService.getShips(userId);
//
//        return ResponseEntity.ok(ApiResponse.success("배 리스트 조회에 성공하였습니다.",shipListResponse));
//    }
    @GetMapping
    public ResponseEntity<ApiResponse<ShipListResponse>> getShips(){
        /**
         * 임시
         */
        Long userId = 2L;
        ShipListResponse shipListResponse = shipService.getShips(userId);

        return ResponseEntity.ok(ApiResponse.success("배 리스트 조회에 성공하였습니다.",shipListResponse));
    }
}
