package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class DuplicateSchedule extends BusinessException {
    public DuplicateSchedule() {
        super(ErrorCode.DUPLICATE_SCHEDULE);
    }
}
