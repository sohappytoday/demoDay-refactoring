package com.lamarfishing.core.log.message.dto.command;

import com.lamarfishing.core.log.message.domain.Result;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageLogDto {

    private String phone;
    private String content;
    private LocalDateTime timeStamp;
    private Result result;
}
