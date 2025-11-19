package com.lamarfishing.core.message.mapper;

import com.lamarfishing.core.message.domain.MessageLog;
import com.lamarfishing.core.message.dto.command.MessageCommonDto;

public class MessageMapper {
    public static MessageCommonDto toMessage(MessageLog message){
        return MessageCommonDto.builder()
                .timeStamp(message.getTimeStamp())
                .recipientPhone(message.getRecipientPhone())
                .content(message.getContent())
                .build();
    }
}
