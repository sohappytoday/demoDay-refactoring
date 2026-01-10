package com.lamarfishing.core.schedule.dto.common;

import com.lamarfishing.core.reservation.dto.common.ReservationDetailDto;
import com.lamarfishing.core.reservation.dto.query.ReservationDetailFlat;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 예약 상세 조회에 사용되는 dto
 */
@Data
@Builder
public class ReservationDetailScheduleDto {
    private LocalDateTime departure;
    private DayOfWeek dayOfWeek;
    private Integer tide;

    public static ReservationDetailScheduleDto from(ReservationDetailFlat dto) {
        return ReservationDetailScheduleDto.builder()
                .departure(dto.getDeparture())
                .dayOfWeek(dto.getDeparture().getDayOfWeek())
                .tide(dto.getTide())
                .build();
    }
}
