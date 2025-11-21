package com.lamarfishing.core.message.service;

import com.lamarfishing.core.message.dto.command.MessageCommonDto;
import com.lamarfishing.core.message.dto.exception.MessageSendFailedException;
import com.lamarfishing.core.schedule.domain.Status;
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

    public List<MessageCommonDto> sendMessage(List<String> phones, Status status) {

        List<String> failedPhones = new ArrayList<>();
        List<MessageCommonDto> results = new ArrayList<>();
        String content = status.message();

        for (String to : phones) {

            Message msg = new Message();
            msg.setFrom(senderNumber);
            msg.setTo(to);
            msg.setText(content);

            try {
                messageService.send(msg);
            } catch (Exception e) {
                failedPhones.add(to);
            }
        }

        if (!failedPhones.isEmpty()) {
            throw new MessageSendFailedException("전송 실패: " + failedPhones, failedPhones);
        }
        return results;
    }
}
