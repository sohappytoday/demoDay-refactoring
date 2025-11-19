package com.lamarfishing.core.message.dto.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class MessageSendFailedException extends BusinessException {
    public MessageSendFailedException(String message) {
        super(ErrorCode.MESSAGE_SEND_FAILED, message);
    }
}
