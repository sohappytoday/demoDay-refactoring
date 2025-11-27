package com.lamarfishing.core.user.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.reservation.service.ReservationQueryService;
import com.lamarfishing.core.reservation.dto.command.ReservationSimpleDto;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.lamarfishing.core.user.dto.request.ChangeNicknameRequest;
import com.lamarfishing.core.user.dto.response.MyProfileResponse;
import com.lamarfishing.core.user.service.UserCommandService;
import com.lamarfishing.core.user.service.UserQueryService;
import com.lamarfishing.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.lamarfishing.core.reservation.domain.Reservation.Process;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;
    private final ReservationQueryService reservationQueryService;
    private final UserService userService;
    private final UserCommandService userCommandService;

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        Long userId = userService.findUserId(authenticatedUser);
        MyProfileResponse response = MyProfileResponse.from(userQueryService.getMyProfile(userId));

        return ResponseEntity.ok(ApiResponse.success("프로필 조회에 성공하였습니다.", response));
    }

    //내 예약 검색하기
    @GetMapping("/me/reservations")
    public ResponseEntity<ApiResponse<PageResponse<ReservationSimpleDto>>> getMyReservations(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                                                                             Process process, Pageable pageable) {

        Long userId = userService.findUserId(authenticatedUser);
        Page<ReservationSimpleDto> pageResult = reservationQueryService.getMyReservations(userId, process, pageable);

        return ResponseEntity.ok(ApiResponse.success("나의 예약 목록 조회를 성공하였습니다.", PageResponse.from(pageResult)));
    }

    @PatchMapping("/me/profile-nickname")
    public ResponseEntity<ApiResponse<Void>> changeNickname(@AuthenticationPrincipal AuthenticatedUser authenticatedUser, @RequestBody ChangeNicknameRequest req) {

        Long userId=userService.findUserId(authenticatedUser);
        userCommandService.changeNickname(userId, req.getNickname());

        return ResponseEntity.ok().body(ApiResponse.success("나의 프로필 수정에 성공하였습니다."));
    }
}
