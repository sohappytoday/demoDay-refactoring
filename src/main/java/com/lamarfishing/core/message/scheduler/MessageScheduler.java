package com.lamarfishing.core.message.scheduler;

import com.lamarfishing.core.message.service.MessageService;
import com.lamarfishing.core.reservation.service.ReservationService;
import com.lamarfishing.core.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final ReservationService reservationService;

    /**
     * 입금 기한 만료 24시간 전 (4일 전)
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendPaymentDeadlineWarning(){
        reservationService.sendPaymentDeadlineWarning();
    }

    /**
     * 입금 기한 만료 (3일 전)
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendPaymentExpiredNotification(){
        reservationService.sendPaymentExpiredNotification();
    }

}
