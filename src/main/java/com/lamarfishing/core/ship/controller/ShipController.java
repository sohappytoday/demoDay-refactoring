package com.lamarfishing.core.ship.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.request.DeleteShipRequest;
import com.lamarfishing.core.ship.dto.request.UpdateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipDetailResponse;
import com.lamarfishing.core.ship.service.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    /**
     * 배 리스트 조회 / 스케쥴 생성할 때 필요한 조회
     */
//   @GetMapping
//    public ResponseEntity<ApiResponse<PageResponse<ShipDetailResponse>>> getShips(@RequestHeader Long userId, Pageable pageable){
//
//        PageResponse<ShipDetailResponse> shipListResponse = shipService.getShips(userId, pageable);
//
//        return ResponseEntity.ok(ApiResponse.success("배 리스트 조회에 성공하였습니다.",shipListResponse));
//    }
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ShipDetailResponse>>> getShips(Pageable pageable){

        Long userId = 2L;
        PageResponse<ShipDetailResponse> shipListResponse = shipService.getShips(userId, pageable);

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
    public ResponseEntity<ApiResponse<Void>> createShip(@RequestBody CreateShipRequest req){

        Long userId = 2L;
        shipService.createShip(userId, req.getFishType(), req.getPrice(), req.getMaxHeadCount(), req.getNotification());
        return ResponseEntity.ok(ApiResponse.success("배 생성에 성공하였습니다."));
    }

    /**
     * 배 수정 api
     */
//    @PutMapping("/{shipId}")
//    public ResponseEntity<ApiResponse<Void>>  updateShip(@RequestHeader Long userId,
//                                                         @RequestBody UpdateShipRequest request,
//                                                         @PathVariable Long shipId){
//        shipService.updateShip(userId, shipId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("배 수정에 성공하였습니다."));
//    }

    @PutMapping("/{shipId}")
    public ResponseEntity<ApiResponse<Void>> updateShip(@RequestBody UpdateShipRequest req,
                                                        @PathVariable Long shipId){
        Long userId = 2L;
        shipService.updateShip(userId, shipId, req.getFishType(), req.getPrice(), req.getMaxHeadCount(), req.getNotification());

        return ResponseEntity.ok(ApiResponse.success("배 수정에 성공"));
    }

    /**
     * 배 삭제 api
     */
//    @PostMapping("/delete")
//    public ResponseEntity<ApiResponse<Void>> deleteShip(@RequestHeader Long userId,
//                                                        @RequestBody DeleteShipRequest request){
//        shipService.deleteShip(userId, request);
//
//        return ResponseEntity.ok(ApiResponse.success("배 삭제에 성공하였습니다."));
//    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteShip(@RequestBody DeleteShipRequest request){
        Long userId = 2L;
        List<Long> shipIds = request.getShipIds();
        shipService.deleteShip(userId, shipIds);

        return ResponseEntity.ok(ApiResponse.success("배 삭제에 성공하였습니다."));
    }


}
