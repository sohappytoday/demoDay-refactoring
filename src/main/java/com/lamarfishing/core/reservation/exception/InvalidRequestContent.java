package com.lamarfishing.core.reservation.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidRequestContent extends BusinessException {
    public InvalidRequestContent() {
        super(ErrorCode.INVALID_REQUEST_CONTENT);
    }
}
