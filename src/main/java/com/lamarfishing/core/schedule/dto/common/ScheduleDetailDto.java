package com.lamarfishing.core.schedule.dto.common;

import com.lamarfishing.core.schedule.domain.Type;
import com.lamarfishing.core.schedule.dto.query.ScheduleDetailFlat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 출항 일정 상세 조회에 사용되는 dto
 */
@Getter
@Builder
public class ScheduleDetailDto {
    private String schedulePublicId;
    private LocalDateTime departure;
    private Integer tide;
    private Integer remainCount;
    private Type type;

    private ScheduleDetailDto(String schedulePublicId, LocalDateTime departure, Integer tide, Integer remainCount, Type type) {
        this.schedulePublicId = schedulePublicId;
        this.departure = departure;
        this.tide = tide;
        this.remainCount = remainCount;
        this.type = type;
    }

    public static ScheduleDetailDto from(ScheduleDetailFlat dto){
        return ScheduleDetailDto.builder()
                .schedulePublicId(dto.getSchedulePublicId())
                .departure(dto.getDeparture())
                .tide(dto.getTide())
                .remainCount(dto.getRemainHeadCount())
                .type(dto.getType())
                .build();
    }
}
