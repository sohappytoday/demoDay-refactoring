package com.lamarfishing.core.reservation.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class ReservationNotFound extends BusinessException {
    public ReservationNotFound() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}
