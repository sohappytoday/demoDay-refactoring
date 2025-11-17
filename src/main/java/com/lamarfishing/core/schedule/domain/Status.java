package com.lamarfishing.core.schedule.domain;

public enum Status {
    WAITING, // 출항 전
    CONFIRMED, // 출항 확정
    DELAYED, // 출항 보류
    CANCELED, // 출항 취소
}
