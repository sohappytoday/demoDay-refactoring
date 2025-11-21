package com.lamarfishing.core.schedule.domain;

public enum Status {
    WAITING("출항 대기 중입니다."), // 출항 전
    CONFIRMED("출항이 확정되었습니다."), // 출항 확정
    DELAYED("출항이 지연되었습니다."), // 출항 보류
    CANCELED("출항이 취소되었습니다."); // 출항 취소

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
