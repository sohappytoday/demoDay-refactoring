package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidDepartureRequest extends BusinessException {
    public InvalidDepartureRequest() {
        super(ErrorCode.INVALID_REQUEST_CONTENT);
    }
}
