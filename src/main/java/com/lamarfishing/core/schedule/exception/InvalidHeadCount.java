package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidHeadCount extends BusinessException {
    public InvalidHeadCount() {
        super(ErrorCode.INVALID_HEADCOUNT);
    }
}
