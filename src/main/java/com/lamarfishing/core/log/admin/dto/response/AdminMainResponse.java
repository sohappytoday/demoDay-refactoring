package com.lamarfishing.core.log.admin.dto.response;

import com.lamarfishing.core.log.statistic.dto.MainInfoDto;
import com.lamarfishing.core.log.statistic.dto.StatisticDto;
import com.lamarfishing.core.log.statistic.dto.TodayInfoDto;
import com.lamarfishing.core.schedule.dto.command.TodayScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminMainResponse {

    TodayScheduleDto todaySchedule;
    TodayInfoDto todayInfo;
    MainInfoDto mainInfo;
    FinalStatisticDto statistic;

    private AdminMainResponse(TodayScheduleDto todaySchedule, TodayInfoDto todayInfo, MainInfoDto mainInfo, FinalStatisticDto statistic) {
        this.todaySchedule = todaySchedule;
        this.todayInfo = todayInfo;
        this.mainInfo = mainInfo;
        this.statistic = statistic;
    }

    public static AdminMainResponse from(TodayScheduleDto todaySchedule, TodayInfoDto todayInfo, MainInfoDto mainInfo, StatisticDto statistic) {
        return new AdminMainResponse(todaySchedule, todayInfo, mainInfo, FinalStatisticDto.from(statistic));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinalStatisticDto {
        int dailyVisited;
        double reservedRate;
        int dailySales;
        int monthlySales;

        private FinalStatisticDto(StatisticDto dto) {
            this.dailyVisited = dto.getDailyVisited();
            this.reservedRate = (double) dto.getDailyReserved() / dto.getDailyVisited();
            this.dailySales = dto.getDailySales();
            this.monthlySales = dto.getMonthlySales();
        }

        public static FinalStatisticDto from(StatisticDto dto) {
            return new FinalStatisticDto(dto);
        }
    }
}
