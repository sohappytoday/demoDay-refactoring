package com.lamarfishing.core.ship.dto.result;

import com.lamarfishing.core.schedule.dto.query.EarlyReservationPopupFlatDto;
import lombok.Builder;
import lombok.Data;
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

    public static ReservationShipDto from(EarlyReservationPopupFlatDto dto){
        return ReservationShipDto.builder()
                .fishType(dto.getFishType())
                .price(dto.getPrice())
                .notification(dto.getNotification())
                .build();
    }
}
