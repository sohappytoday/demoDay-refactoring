package com.lamarfishing.core.log.message.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "message_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageLog {

    @Id @GeneratedValue
    @Column(name = " message_log_id")
    private Long id;

    @Column(name = "message_log_time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "message_log_recipient_phone")
    private String recipientPhone;

    @Column(name = "message_log_content")
    private String content;

    private MessageLog(LocalDateTime timeStamp, String recipientPhone, String content) {
        this.timeStamp = timeStamp;
        this.recipientPhone = recipientPhone;
        this.content = content;
    }

    public static MessageLog create(String recipientPhone, String content) {
        return new MessageLog(LocalDateTime.now(), recipientPhone, content);
    }
}
