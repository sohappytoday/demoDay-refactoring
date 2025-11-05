package com.lamarfishing.core.schedule.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationCreateResponse {
    private String reservationPublicId;

    public ReservationCreateResponse(String reservationPublicId) {
        this.reservationPublicId = reservationPublicId;
    }
}
