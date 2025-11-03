package com.lamarfishing.core.log.message.dto.response;

import com.lamarfishing.core.log.message.dto.command.MessageCommonDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepartureResponse {
    private List<MessageCommonDto> messages;

}
