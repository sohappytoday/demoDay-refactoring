package com.lamarfishing.core.reservation.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidReservationPublicId extends BusinessException {
    public InvalidReservationPublicId() {
        super(ErrorCode.INVALID_PUBLICId);
    }
}
