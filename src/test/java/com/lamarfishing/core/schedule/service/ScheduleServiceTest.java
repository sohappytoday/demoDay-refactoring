package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @DisplayName("getScheduleDetail-Response Body 테스트")
    @Test
    void getScheduleDetail_적절한_값이_매핑() {

    }
}