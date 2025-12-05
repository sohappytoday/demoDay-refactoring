package com.lamarfishing.core.log.admin.controller;

import com.lamarfishing.core.common.dto.response.ApiResponse;
import com.lamarfishing.core.log.admin.dto.response.AdminMainResponse;
import com.lamarfishing.core.log.admin.service.AdminService;
import com.lamarfishing.core.log.statistic.dto.MainInfoDto;
import com.lamarfishing.core.log.statistic.dto.StatisticDto;
import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.schedule.dto.common.TodayScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final StatisticService statisticService;

    @GetMapping("/admin/main")
    public ResponseEntity<ApiResponse<AdminMainResponse>> adminMain() {

        TodayScheduleDto todaySchedule = adminService.getTodaySchedule();
        TodayInfoDto todayInfo = statisticService.getTodayInfo();
        MainInfoDto mainInfo = statisticService.getMainInfo();
        StatisticDto statistic = statisticService.getStatistic();

        AdminMainResponse response = AdminMainResponse.from(todaySchedule, todayInfo, mainInfo, statistic);

        return ResponseEntity.ok(ApiResponse.success("관리자 대시보드 조회에 성공하였습니다.", response));
    }
}
