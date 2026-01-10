package com.lamarfishing.core.ship.dto.result;

import com.lamarfishing.core.reservation.dto.query.ReservationDetailFlat;
import lombok.Builder;
import lombok.Data;

/**
 * 예약 상세 조회에 사용되는 dto
 */
@Data
@Builder
public class ReservationDetailShipDto {
    private String fishType;
    private String notification;

    public static ReservationDetailShipDto from(ReservationDetailFlat dto){
        return ReservationDetailShipDto.builder()
                .fishType(dto.getFishType())
                .notification(dto.getNotification())
                .build();
    }
}
