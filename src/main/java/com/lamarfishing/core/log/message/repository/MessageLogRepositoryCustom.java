package com.lamarfishing.core.log.message.repository;

import com.lamarfishing.core.log.message.domain.Result;
import com.lamarfishing.core.log.message.dto.command.MessageLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MessageLogRepositoryCustom {
    Page<MessageLogDto> findMessageLog(LocalDateTime from, LocalDateTime to, Result result, Pageable pageable);
}
