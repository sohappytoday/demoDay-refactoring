package com.lamarfishing.core.coupon.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class InvalidCouponStatus extends BusinessException {
    public InvalidCouponStatus() {
        super(ErrorCode.INVALID_COUPON_STATUS);
    }
}
