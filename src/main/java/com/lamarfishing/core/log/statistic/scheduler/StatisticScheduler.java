package com.lamarfishing.core.log.statistic.scheduler;

import com.lamarfishing.core.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticScheduler {

    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void statisticPaymentDeadlineWarning(){reservationService.statisticPaymentDeadlineWarning();}

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void statisticPaymentExpiredNotification(){reservationService.statisticPaymentExpiredNotification();}

}
