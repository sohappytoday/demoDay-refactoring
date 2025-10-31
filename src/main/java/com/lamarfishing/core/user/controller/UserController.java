package com.lamarfishing.core.user.controller;

import com.lamarfishing.core.common.ApiResponse;
import com.lamarfishing.core.user.dto.command.MyProfileDto;
import com.lamarfishing.core.user.dto.response.MyProfileResponse;
import com.lamarfishing.core.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile() {

        MyProfileResponse response = MyProfileResponse.from(userQueryService.getMyProfile());

        return ResponseEntity.ok(ApiResponse.success("프로필 조회에 성공하였습니다.", response));
    }
}
