package com.lamarfishing.core.log.message.repository;

import com.lamarfishing.core.message.domain.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog,Long> {
}
