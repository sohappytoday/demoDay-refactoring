package com.lamarfishing.core.ship.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class ShipNotFound extends BusinessException {
    public ShipNotFound() {
        super(ErrorCode.SHIP_NOT_FOUND);
    }
}
