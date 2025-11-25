package com.lamarfishing.core.log.statistic.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StatisticAspect {


    // 일간 방문자 수 집계
    @Before("execution(* com.lamarfishing.core.schedule.controller.ScheduleController.getSchedules(..))")
    public void beforeMain() {

    }

    // 일간 예약자 수, 매출 집계
    @AfterReturning("execution(* com.lamarfishing.core.schedule.controller.ReservationPopupController.createReservation(..))")
    public void afterReservation() {

    }

    // 일간 입금 확인자 수 집계
//    @AfterReturning("execution(* com.lamarfishing.core.")
    public void afterDeposit() {

    }
}
