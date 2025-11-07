package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.command.ReservationCommonDto;
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
        Reservation reservation1 = Reservation.create(3, "김지오입니다.", 270000, Reservation.Process.RESERVE_COMPLETED, user1, schedule,null);
        Reservation reservation2 = Reservation.create(5, "원종윤입니다.", 450000, Reservation.Process.RESERVE_COMPLETED, user2, schedule,null);
        Reservation reservation3 = Reservation.create(8, "김승규입니다.", 720000, Reservation.Process.RESERVE_COMPLETED, user3, schedule,null);

        String publicId = schedule.getPublicId();
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(reservationRepository.findBySchedule(schedule))
                .thenReturn(List.of(reservation1, reservation2,reservation3));

        //when
        ScheduleDetailResponse response = scheduleService.getScheduleDetail(publicId);

        ReservationCommonDto resDto1 = response.getReservations().get(0);
        ReservationCommonDto  resDto2 = response.getReservations().get(1);
        ReservationCommonDto  resDto3 = response.getReservations().get(2);

        //then

        //common
        assertThat(response).isNotNull();

        //shipCommonDto
        assertThat(response.getShip().getShipId()).isEqualTo(ship.getId());
        assertThat(response.getShip().getFishType()).isEqualTo(ship.getFishType());
        assertThat(response.getShip().getPrice()).isEqualTo(ship.getPrice());
        //scheduleDetailDto
        assertThat(response.getSchedule().getSchedulePublicId()).isEqualTo(publicId);
        assertThat(response.getSchedule().getDeparture()).isEqualTo(schedule.getDeparture());
        assertThat(response.getSchedule().getTide()).isEqualTo(schedule.getTide());
        assertThat(response.getSchedule().getRemainCount()).isEqualTo(ship.getMaxHeadCount()-schedule.getCurrentHeadCount());
        //List<Reservation>
        assertThat(resDto1.getGrade()).isEqualTo(user1.getGrade());
        assertThat(resDto1.getNickname()).isEqualTo(user1.getNickname());
        assertThat(resDto1.getProcess()).isEqualTo(reservation1.getProcess());
        assertThat(resDto1.getHeadCount()).isEqualTo(reservation1.getHeadCount());
        assertThat(resDto1.getReservationPublicId()).isEqualTo(reservation1.getPublicId());

        assertThat(resDto2.getGrade()).isEqualTo(user2.getGrade());
        assertThat(resDto2.getNickname()).isEqualTo(user2.getNickname());
        assertThat(resDto2.getProcess()).isEqualTo(reservation2.getProcess());
        assertThat(resDto2.getHeadCount()).isEqualTo(reservation2.getHeadCount());
        assertThat(resDto2.getReservationPublicId()).isEqualTo(reservation2.getPublicId());

        assertThat(resDto3.getGrade()).isEqualTo(user3.getGrade());
        assertThat(resDto3.getNickname()).isEqualTo(user3.getNickname());
        assertThat(resDto3.getProcess()).isEqualTo(reservation3.getProcess());
        assertThat(resDto3.getHeadCount()).isEqualTo(reservation3.getHeadCount());
        assertThat(resDto3.getReservationPublicId()).isEqualTo(reservation3.getPublicId());

    }
}