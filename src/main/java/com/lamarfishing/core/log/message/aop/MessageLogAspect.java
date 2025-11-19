package com.lamarfishing.core.log.message.aop;

import com.lamarfishing.core.message.domain.MessageLog;
import com.lamarfishing.core.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.log.message.repository.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MessageLogAspect {

    private final MessageLogRepository messageLogRepository;

    @AfterReturning(
            pointcut =
                    "execution(java.util.List<com.lamarfishing.core.message.dto.command.MessageCommonDto> " +
                    "com.lamarfishing.core.message.service.MessageService.send*(..))",
            returning = "result"
    )
    public void successLog(List<MessageCommonDto> result) {

        List<MessageLog> logs = result.stream().map(
                message -> MessageLog.create(message.getRecipientPhone(), message.getContent())
        ).toList();

        messageLogRepository.saveAll(logs);
    }


    // 실패 이력을 재활용할 일 X
    @AfterThrowing(
            pointcut = "execution(* com.lamarfishing.core.message.service.MessageService.*(..))",
            throwing = "ex"
    )
    public void failureLog(JoinPoint joinPoint, Throwable ex) {
        return;
    }

}
