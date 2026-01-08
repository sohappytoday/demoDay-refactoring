package com.lamarfishing.core.ship.dto.result;

import com.lamarfishing.core.schedule.dto.query.ReservationPopupFlatDto;
import lombok.Builder;
import lombok.Getter;

/**
 * 예약 팝업 조회에 사용되는 dto
 */
@Getter
@Builder
public class ReservationShipDto {
    private String fishType;
    private Integer price;
    private String notification;

    public static ReservationShipDto from(ReservationPopupFlatDto dto){
        return ReservationShipDto.builder()
                .fishType(dto.getFishType())
                .price(dto.getPrice())
                .notification(dto.getNotification())
                .build();
    }
}
