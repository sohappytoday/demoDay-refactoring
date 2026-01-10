package com.lamarfishing.core.reservation.dto.common;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.query.ReservationDetailFlat;
import lombok.Builder;
import lombok.Data;

/**
 * 예약 상세 조회에 사용되는 dto
 */
@Data
@Builder
public class ReservationDetailDto {
    private String username;
    private String nickname;
    private String phone;
    private Integer headCount;
    private Long couponId;
    private String request;
    private Integer totalPrice;
    private Reservation.Process process;

    public static ReservationDetailDto from(ReservationDetailFlat dto){
        return ReservationDetailDto.builder()
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .headCount(dto.getHeadCount())
                .couponId(dto.getCouponId())
                .request(dto.getRequest())
                .totalPrice(dto.getTotalPrice())
                .process(dto.getProcess())
                .build();
    }
}
