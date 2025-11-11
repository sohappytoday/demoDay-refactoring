package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidDepartureTime extends BusinessException {
    public InvalidDepartureTime() {
        super(ErrorCode.INVALID_DEPARTURE_TIME);
    }
}
