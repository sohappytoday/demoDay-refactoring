package com.lamarfishing.core.schedule.dto.response;

import com.lamarfishing.core.message.dto.command.MessageCommonDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DepartureResponse {
    private List<MessageCommonDto> messages;

    public static DepartureResponse from(List<MessageCommonDto> messages) {
        return DepartureResponse.builder()
                .messages(messages)
                .build();
    }
}
