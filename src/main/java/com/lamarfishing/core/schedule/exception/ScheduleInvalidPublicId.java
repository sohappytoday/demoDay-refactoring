package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class ScheduleInvalidPublicId extends BusinessException {
    public ScheduleInvalidPublicId() {super(ErrorCode.INVALID_PUBLICId);}
}
