package com.lamarfishing.core.schedule.repository;

import com.lamarfishing.core.schedule.dto.common.ScheduleMainDto;
import com.lamarfishing.core.schedule.dto.common.TodayScheduleDto;
import com.lamarfishing.core.schedule.dto.query.ReservationPopupFlatDto;
import com.lamarfishing.core.schedule.dto.query.ScheduleDetailFlat;
import com.lamarfishing.core.schedule.dto.result.DepartureTimeResult;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleMainDto> getSchedules(LocalDateTime from, LocalDateTime to);
    public TodayScheduleDto getTodaySchedule();

    /**
     * 다음 출항 일정 조회
     */
    DepartureTimeResult findNextDeparture();
    ScheduleDetailFlat findScheduleDetailFlat(Long id);
    ReservationPopupFlatDto getScheduleAndShipPopup(Long scheduleId);
}
