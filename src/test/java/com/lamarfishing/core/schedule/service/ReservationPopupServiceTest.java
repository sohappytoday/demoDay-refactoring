package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.schedule.controller.ReservationPopupController;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.response.ReservationPopupResponse;
import com.lamarfishing.core.schedule.exception.ScheduleInvalidPublicId;
import com.lamarfishing.core.schedule.repository.ScheduleRepository;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.CsvSources;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationPopupServiceTest {

    @InjectMocks
    private ReservationPopupService reservationPopupService;

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;

    @DisplayName("getReservationPopup: publicId가 schedule이 아니면 ScheduleInvalidPublicId 예외를 발생시킨다.")
    @Test
    void getReservationPopup_publicId가_schedule_것이_아니면_예외() {
        // given
        Long userId = 1L;
        String grade = "BASIC";
        String invalidPublicId = "res-123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.getReservationPopup(userId, grade, invalidPublicId)
        ).isInstanceOf(ScheduleInvalidPublicId.class);
    }

    @DisplayName("getReservationPopup: userGrade가 Grade에 없으면 InvalidUserGrade 예외를 발생시킨다.")
    @Test
    void getReservationPopup_userGrade가_Grade_type으로_변환_불가시_예외(){
        // given
        Long userId = 1L;
        String grade = "HELLO";
        String invalidPublicId = "sch-123123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.getReservationPopup(userId, grade, invalidPublicId)
        ).isInstanceOf(InvalidUserGrade.class);
    }

    @DisplayName("getReservationPopup: userGrade가 Grade에 없으면 InvalidUserGrade 예외를 발생시킨다.")
    @Test
    void getReservationPopup_InvalidUserGrade(){
        // given
        Long userId = 1L;
        String grade = "HELLO";
        String invalidPublicId = "sch-123123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.getReservationPopup(userId, grade, invalidPublicId)
        ).isInstanceOf(InvalidUserGrade.class);
    }

    @DisplayName("getReservationPopup: userGrade 문자열이 User.Grade로 정상 파싱된다")
    @ParameterizedTest
    @CsvSource({
            "guest, GUEST",
            "Basic, BASIC",
            "ADMIN, ADMIN",
            "viP, VIP"
    })
    void getReservationPopup_UserGradeParsing_Success(String input, String expected) {
        // given
        User.Grade expectedGrade = User.Grade.valueOf(expected);

        // when
        User.Grade actual = User.Grade.valueOf(input.toUpperCase());

        // then
        assertThat(actual).isEqualTo(expectedGrade);
    }

    @DisplayName("getReservationPopup: 회원 정상 조회")
    @Test
    void getReservationPopup_BASIC() {
        //given
        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");

        Schedule schedule = Schedule.create(LocalDateTime.of(2025,11,5,0,0),
                5, 3, Schedule.Status.WAITING, Schedule.Type.NORMAL, ship);

        Long userId = 1L;
        String grade = "BASIC";
        String publicId = schedule.getPublicId();

        User user = mock(User.class);
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(couponRepository.findByUser(user)).thenReturn(List.of(mock(Coupon.class)));

        //when
        ReservationPopupResponse response =
                reservationPopupService.getReservationPopup(userId, grade, publicId);

        //then
        assertThat(response).isNotNull();
        //ship
        assertThat(response.getShip().getShipId()).isEqualTo(ship.getId());
        assertThat(response.getShip().getFishType()).isEqualTo(ship.getFishType());
        assertThat(response.getShip().getPrice()).isEqualTo(ship.getPrice());
        assertThat(response.getShip().getRemainHeadCount()).isEqualTo(15);  // 20 - 5



    }
}