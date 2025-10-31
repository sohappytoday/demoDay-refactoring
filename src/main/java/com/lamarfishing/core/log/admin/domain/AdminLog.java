package com.lamarfishing.core.log.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "admin_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLog {

    @Id @GeneratedValue
    @Column(name = "admin_log_id")
    private Long id;

    @Column(name = "admin_log_time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "admin_log_account")
    private String account;

    @Column(name = "admin_log_ip")
    private String ip;

    private AdminLog(LocalDateTime timeStamp, String account, String ip) {
        this.timeStamp = timeStamp;
        this.account = account;
        this.ip = ip;
    }

    public static AdminLog create(String account, String ip) {
        return new AdminLog(LocalDateTime.now(), account, ip);
    }
}
