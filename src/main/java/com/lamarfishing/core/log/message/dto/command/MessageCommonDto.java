package com.lamarfishing.core.log.message.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageCommonDto {
    private LocalDateTime timeStamp;
    private String recipientPhone;
    private String content;

}
