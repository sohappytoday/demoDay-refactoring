package com.lamarfishing.core.message.scheduler;

import com.lamarfishing.core.reservation.service.command.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final ReservationCommandService reservationCommandService;

    /**
     * 입금 기한 만료 24시간 전 (4일 전)
     */
    //@Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void sendPaymentDeadlineWarning(){
        reservationCommandService.sendPaymentDeadlineWarning();
    }

    /**
     * 입금 기한 만료 (3일 전)
     */
    //@Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void sendPaymentExpiredNotification(){
        reservationCommandService.sendPaymentExpiredNotification();
    }

}
