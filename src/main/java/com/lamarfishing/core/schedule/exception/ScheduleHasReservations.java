package com.lamarfishing.core.schedule.exception;

import com.lamarfishing.core.common.exception.BusinessException;
import com.lamarfishing.core.common.exception.ErrorCode;

public class ScheduleHasReservations extends BusinessException {
    public ScheduleHasReservations() {super(ErrorCode.SCHEDULE_HAS_RESERVATIONS);}
}
