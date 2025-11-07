package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidSchedulePublicId extends BusinessException {
    public InvalidSchedulePublicId() {super(ErrorCode.INVALID_PUBLICId);}
}
