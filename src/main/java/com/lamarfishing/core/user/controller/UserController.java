package com.lamarfishing.core.user.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.reservation.service.ReservationQueryService;
import com.lamarfishing.core.user.dto.command.MyReservationDto;
import com.lamarfishing.core.user.dto.response.MyProfileResponse;
import com.lamarfishing.core.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.lamarfishing.core.reservation.domain.Reservation.Process;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;
    private final ReservationQueryService reservationQueryService;

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile() {

        MyProfileResponse response = MyProfileResponse.from(userQueryService.getMyProfile());

        return ResponseEntity.ok(ApiResponse.success("프로필 조회에 성공하였습니다.", response));
    }

    //내 예약 검색하기
    @GetMapping("/me/reservations")
    public ResponseEntity<ApiResponse<PageResponse<MyReservationDto>>> getMyReservations(Process process, Pageable pageable) {
        Page<MyReservationDto> pageResult = reservationQueryService.getReservations(1L, process, pageable);
        return ResponseEntity.ok(ApiResponse.success("나의 예약 목록 조회를 성공하였습니다.", PageResponse.from(pageResult)));
    }
}
