package com.lamarfishing.core.reservation.dto.request;

import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class ReservationProcessUpdateRequest {
    @NotNull
    private Reservation.Process process;
}
