package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class UnauthorizedPopupAccess extends BusinessException {
    public UnauthorizedPopupAccess() {
        super(ErrorCode.UNAUTHORIZED_SCHEDULE_ACCESS);
    }
}
