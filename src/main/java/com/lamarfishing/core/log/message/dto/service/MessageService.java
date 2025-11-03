package com.lamarfishing.core.log.message.dto.service;

import com.lamarfishing.core.log.message.domain.MessageLog;
import com.lamarfishing.core.log.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.log.message.dto.exception.MessageSendFailedException;
import com.lamarfishing.core.log.message.mapper.MessageMapper;
import com.lamarfishing.core.log.message.repository.MessageLogRepository;
import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private DefaultMessageService messageService;
    private final MessageLogRepository messageLogRepository;

    @Value("${solapi.sender}")
    private String senderNumber;

    @Value("${solapi.api-key}")
    private String apiKey;

    @Value("${solapi.api-secret}")
    private String apiSecret;

    @PostConstruct
    public void initSolapi() {
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
    }

    public List<MessageCommonDto> sendDepartureConfirmationMessages(List<String> phones) {
        String content = "아 배고프다~ 밥 먹고 코딩할까 (출항확정)";
        List<String> sendFailedPhones = new ArrayList<>();
        List<MessageCommonDto> results = new ArrayList<>();

        for (String to : phones) {

            Message msg = new Message();
            msg.setFrom(senderNumber);
            msg.setTo(to);
            msg.setText(content);

            try {
                messageService.send(msg);
                MessageLog messageLog = MessageLog.create(to,content);
                messageLogRepository.save(messageLog);
                results.add(MessageMapper.toMessage(messageLog));
            } catch (SolapiMessageNotReceivedException e) {
                sendFailedPhones.add(to);
                throw new MessageSendFailedException("문자 전송 실패: " + e.getFailedMessageList());
            } catch (Exception e) {
                sendFailedPhones.add(to);
                throw new MessageSendFailedException("오류: " + e.getMessage());
            }
        }
        return results;
    }
}
