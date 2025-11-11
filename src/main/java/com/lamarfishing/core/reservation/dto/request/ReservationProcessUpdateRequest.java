package com.lamarfishing.core.reservation.dto.request;

import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationProcessUpdateRequest {
    private Reservation.Process process;
}
