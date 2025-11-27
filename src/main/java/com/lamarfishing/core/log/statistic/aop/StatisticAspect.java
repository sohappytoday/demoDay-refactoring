package com.lamarfishing.core.log.statistic.aop;

import com.lamarfishing.core.log.statistic.domain.Statistic;
import com.lamarfishing.core.log.statistic.service.StatisticService;
import com.lamarfishing.core.reservation.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
@RequiredArgsConstructor
public class StatisticAspect {

    private final StatisticService statisticService;

    // 일간 방문자 수 집계
    @Before("execution(* com.lamarfishing.core.schedule.controller.ScheduleController.getSchedules(..))")
    public void beforeMain() {
        statisticService.addVisited(LocalDate.now());
    }

    // 일간 입금 확인자 수 집계
    @AfterReturning(value = "execution(* com.lamarfishing.core.reservation.service.ReservationService.changeReservationProcess(..)) "
            + "&& args(userId, publicId, requestProcess)", argNames = "userId,publicId,requestProcess")
    public void afterDeposit(Long userId, String publicId, Reservation.Process requestProcess) {
        if (requestProcess != Reservation.Process.DEPOSIT_COMPLETED) {
            statisticService.addDeposited(LocalDate.now());
        }
    }

}
