package com.lamarfishing.core.log.message.aop;

import com.lamarfishing.core.log.message.domain.MessageLog;
import com.lamarfishing.core.log.message.domain.Result;
import com.lamarfishing.core.log.message.repository.MessageLogRepository;
import com.lamarfishing.core.message.dto.exception.MessageSendFailedException;
import com.lamarfishing.core.schedule.domain.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MessageLogAspect {

    private final MessageLogRepository messageLogRepository;

    @AfterReturning(
            pointcut = "execution(* com.lamarfishing.core.message.service.MessageService.*(..)) && args(phones, status)"
    )
    public void successLog(List<String> phones, Status status) {

        List<MessageLog> logs = phones.stream().map(phone ->
                MessageLog.create(phone, status.message(), Result.SUCCESS)).toList();

        messageLogRepository.saveAll(logs);
    }

    @AfterThrowing(
            pointcut = "execution(* com.lamarfishing.core.message.service.MessageService.*(..)) && args(phones, status)",
            throwing = "ex"
    )
    public void failureLog(List<String> phones, Status status, MessageSendFailedException ex) {

        List<MessageLog> logs = ex.getFailedPhones().stream().map(phone ->
                MessageLog.create(phone, status.message(), Result.SUCCESS)).toList();

        messageLogRepository.saveAll(logs);
    }

}
