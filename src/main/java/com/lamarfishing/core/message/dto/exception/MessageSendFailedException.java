package com.lamarfishing.core.message.dto.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class MessageSendFailedException extends BusinessException {

    private final List<String> failedPhones;

    public MessageSendFailedException(String message, List<String> failedPhones) {
        super(ErrorCode.MESSAGE_SEND_FAILED, message);
        this.failedPhones = failedPhones;
    }

}
