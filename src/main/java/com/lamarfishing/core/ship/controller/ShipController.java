package com.lamarfishing.core.ship.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.request.DeleteShipRequest;
import com.lamarfishing.core.ship.dto.request.UpdateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipDetailResponse;
import com.lamarfishing.core.ship.service.ShipService;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;
    private final UserService userService;
    /**
     * 배 리스트 조회 / 스케쥴 생성할 때 필요한 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ShipDetailResponse>>> getShips(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                  Pageable pageable){

        Long userId = userService.findUserId(authenticatedUser);
        Page<ShipDetailDto> pageResult = shipService.getShips(userId, pageable);
        /**
         * 수정해야함 (뭘 수정해야하는지는 11월 27일을 기억할것)
         */
        Page<ShipDetailResponse> response = pageResult.map(ShipDetailResponse::from);

        return ResponseEntity.ok(ApiResponse.success("배 리스트 조회에 성공하였습니다.",PageResponse.from(response)));
    }

    /**
     * 배 생성 api
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createShip(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                        @RequestBody CreateShipRequest req){

        Long userId = userService.findUserId(authenticatedUser);
        shipService.createShip(userId, req.getFishType(), req.getPrice(), req.getMaxHeadCount(), req.getNotification());
        return ResponseEntity.ok(ApiResponse.success("배 생성에 성공하였습니다."));
    }

    /**
     * 배 수정 api
     */
    @PutMapping("/{shipId}")
    public ResponseEntity<ApiResponse<Void>> updateShip(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                        @RequestBody UpdateShipRequest req,
                                                        @PathVariable Long shipId){

        Long userId = userService.findUserId(authenticatedUser);
        shipService.updateShip(userId, shipId, req.getFishType(), req.getPrice(), req.getMaxHeadCount(), req.getNotification());

        return ResponseEntity.ok(ApiResponse.success("배 수정에 성공"));
    }

    /**
     * 배 삭제 api
     */
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteShip(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                        @RequestBody DeleteShipRequest request){

        Long userId = userService.findUserId(authenticatedUser);
        List<Long> shipIds = request.getShipIds();
        shipService.deleteShip(userId, shipIds);

        return ResponseEntity.ok(ApiResponse.success("배 삭제에 성공하였습니다."));
    }


}
