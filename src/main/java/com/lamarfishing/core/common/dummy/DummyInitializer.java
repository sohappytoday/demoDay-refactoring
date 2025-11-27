package com.lamarfishing.core.common.dummy;

import com.lamarfishing.core.coupon.dummy.CouponDummy;
import com.lamarfishing.core.log.statistic.dummy.StatisticDummy;
import com.lamarfishing.core.reservation.dummy.ReservationDummy;
import com.lamarfishing.core.schedule.dummy.ScheduleDummy;
import com.lamarfishing.core.ship.dummy.ShipDummy;
import com.lamarfishing.core.user.dummy.UserDummy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyInitializer {

    private final UserDummy userDummy;
    private final ShipDummy shipDummy;
    private final ScheduleDummy scheduleDummy;
    private final CouponDummy couponDummy;
    private final ReservationDummy reservationDummy;
    private final StatisticDummy statisticDummy;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        userDummy.init();
        shipDummy.init();
        scheduleDummy.init();
        couponDummy.init();
        reservationDummy.init();
        statisticDummy.init();

    }
}
