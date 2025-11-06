package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.response.ScheduleDetailResponse;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.user.domain.User;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        //배 생성
        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");
        //스케쥴 생성
        Schedule schedule = Schedule.create(LocalDateTime.of(2025, 11, 5, 0, 0),
                4, 3, Schedule.Status.WAITING, Schedule.Type.NORMAL, ship);
        //유저 생성
        User user1 = User.create("김지오", "geooeg", User.Grade.GUEST, "01012345678");
        User user2 = User.create("원종윤", "bellyun", User.Grade.BASIC, "01077776777");
        User user3 = User.create("김승규", "wingyu", User.Grade.VIP, "01088889888");
        //예약 생성
        Reservation reservation1 = Reservation.create(3, "김지오입니다.", 270000, Reservation.Process.RESERVE_COMPLETED, user1, schedule);
        Reservation reservation2 = Reservation.create(5, "원종윤입니다.", 450000, Reservation.Process.RESERVE_COMPLETED, user2, schedule);
        Reservation reservation3 = Reservation.create(8, "김승규입니다.", 720000, Reservation.Process.RESERVE_COMPLETED, user3, schedule);

        String publicId = schedule.getPublicId();
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(reservationRepository.findBySchedule(schedule))
                .thenReturn(List.of(reservation1, reservation2,reservation3));

        //when
        ScheduleDetailResponse scheduleDetailResponse = scheduleService.getScheduleDetail(publicId);

        //then

        //common
        assertThat(scheduleDetailResponse).isNotNull();
        assertThat(scheduleDetailResponse.)
    }
}