package com.lamarfishing.core.ship.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipListResponse;
import com.lamarfishing.core.ship.service.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 배 생성 api
     */
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> createShip(@RequestHeader Long userId, CreateShipRequest request){
//
//        shipService.CreateShip(userId, request);
//        return ResponseEntity.ok(ApiResponse.success("배 생성에 성공하였습니다."));
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createShip(CreateShipRequest request){

        Long userId = 2L;
        shipService.CreateShip(userId, request);
        return ResponseEntity.ok(ApiResponse.success("배 생성에 성공하였습니다."));
    }
}
