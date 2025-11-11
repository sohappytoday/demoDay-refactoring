package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidScheduleType extends BusinessException {
    public InvalidScheduleType() {
        super(ErrorCode.INVALID_SCHEDULE_TYPE);
    }
}
