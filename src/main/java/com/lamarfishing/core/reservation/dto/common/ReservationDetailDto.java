package com.lamarfishing.core.reservation.dto.common;

import com.lamarfishing.core.reservation.domain.Reservation;
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
}
