package com.lamarfishing.core.schedule.service;

import com.lamarfishing.core.coupon.domain.Coupon;
import com.lamarfishing.core.coupon.dto.CouponCommonDto;
import com.lamarfishing.core.coupon.mapper.CouponMapper;
import com.lamarfishing.core.coupon.repository.CouponRepository;
import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.schedule.domain.Schedule;
import com.lamarfishing.core.schedule.dto.request.ReservationPopupRequest;
import com.lamarfishing.core.schedule.dto.response.ReservationCreateResponse;
import com.lamarfishing.core.schedule.dto.response.ReservationPopupResponse;
import com.lamarfishing.core.schedule.exception.InvalidSchedulePublicId;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @Mock
    private ReservationRepository reservationRepository;

    /**
     * getReservationPopup
     */
    @DisplayName("getReservationPopup: publicId가 schedule이 아니면 InvalidSchedulePublicId 예외를 발생시킨다.")
    @Test
    void getReservationPopup_publicId가_schedule_것이_아니면_예외() {
        // given
        Long userId = 1L;
        String grade = "BASIC";
        String invalidPublicId = "res-123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.getReservationPopup(userId, grade, invalidPublicId)
        ).isInstanceOf(InvalidSchedulePublicId.class);
    }

    @DisplayName("getReservationPopup: userGrade가 Grade에 없으면 InvalidUserGrade 예외를 발생시킨다.")
    @Test
    void getReservationPopup_userGrade가_Grade_type으로_변환_불가시_예외() {
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

        Schedule schedule = Schedule.create(LocalDateTime.of(2025, 11, 5, 0, 0),
                5, 3, Schedule.Status.WAITING, Schedule.Type.NORMAL, ship);

        User user = User.create("김지오", "geooeg", User.Grade.BASIC, "01012345678");
        Coupon coupon1 = Coupon.create(Coupon.Type.WEEKDAY, user);
        Coupon coupon2 = Coupon.create(Coupon.Type.WEEKEND, user);

        List<Coupon> couponEntities = List.of(coupon1, coupon2);
        List<CouponCommonDto> coupons = couponEntities.stream()
                .map(CouponMapper::toCouponCommonDto)
                .toList();

        Long userId = 1L;
        String grade = "BASIC";
        String publicId = schedule.getPublicId();

        //when
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(couponRepository.findByUserAndStatus(user, Coupon.Status.AVAILABLE)).thenReturn(couponEntities);

        ReservationPopupResponse response =
                reservationPopupService.getReservationPopup(userId, grade, publicId);

        //then
        assertThat(response).isNotNull();
        //ship
        assertThat(response.getShip().getFishType()).isEqualTo(ship.getFishType());
        assertThat(response.getShip().getPrice()).isEqualTo(ship.getPrice());
        assertThat(response.getShip().getNotification()).isEqualTo(ship.getNotification());
        //user
        assertThat(response.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(response.getUser().getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getUser().getGrade()).isEqualTo(user.getGrade());
        assertThat(response.getUser().getPhone()).isEqualTo(user.getPhone());
        assertThat(response.getUser().getCoupons()).isEqualTo(coupons);
        //schedule
        assertThat(response.getSchedulePublicId()).isEqualTo(publicId);
        assertThat(response.getDeparture()).isEqualTo(schedule.getDeparture());
        assertThat(response.getTide()).isEqualTo(schedule.getTide());
        assertThat(response.getRemainHeadCount()).isEqualTo(ship.getMaxHeadCount() - schedule.getCurrentHeadCount());
        assertThat(response.getDayOfWeek()).isEqualTo((schedule.getDeparture().getDayOfWeek()));
        assertThat(response.getTide()).isEqualTo(schedule.getTide());

    }

    @DisplayName("getReservationPopup: 비회원 정상 조회")
    @Test
    void getReservationPopup_GUEST() {
        //given
        String GUEST_USERNAME = "비회원_이름";
        String GUEST_NICKNAME = "비회원_닉네임";
        String GUEST_PHONE = "비회원_전화번호";

        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");

        Schedule schedule = Schedule.create(LocalDateTime.of(2025, 11, 5, 0, 0),
                5, 3, Schedule.Status.WAITING, Schedule.Type.NORMAL, ship);

        User user = User.create("김지오", "geooeg", User.Grade.GUEST, "01012345678");  //사실 없는 객체임

        Long userId = 1L;
        String grade = "GUEST";
        String publicId = schedule.getPublicId();

        //when
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));

        ReservationPopupResponse response =
                reservationPopupService.getReservationPopup(userId, grade, publicId);

        //then
        assertThat(response).isNotNull();
        //ship
        assertThat(response.getShip().getFishType()).isEqualTo(ship.getFishType());
        assertThat(response.getShip().getPrice()).isEqualTo(ship.getPrice());
        assertThat(response.getShip().getNotification()).isEqualTo(ship.getNotification());
        //user
        assertThat(response.getUser().getUsername()).isEqualTo(GUEST_USERNAME);
        assertThat(response.getUser().getNickname()).isEqualTo(GUEST_NICKNAME);
        assertThat(response.getUser().getGrade()).isEqualTo(user.getGrade());
        assertThat(response.getUser().getPhone()).isEqualTo(GUEST_PHONE);
        assertThat(response.getUser().getCoupons()).isEmpty();
        //schedule
        assertThat(response.getSchedulePublicId()).isEqualTo(publicId);
        assertThat(response.getDeparture()).isEqualTo(schedule.getDeparture());
        assertThat(response.getTide()).isEqualTo(schedule.getTide());
        assertThat(response.getRemainHeadCount()).isEqualTo(ship.getMaxHeadCount() - schedule.getCurrentHeadCount());
        assertThat(response.getDayOfWeek()).isEqualTo((schedule.getDeparture().getDayOfWeek()));
        assertThat(response.getTide()).isEqualTo(schedule.getTide());

    }

    /**
     * createReservation
     */
    @DisplayName("createReservation: publicId가 schedule이 아니면 InvalidSchedulePublicId 예외를 발생시킨다.")
    @Test
    void createReservation_publicId가_schedule_것이_아니면_예외() {
        // given
        Long userId = 1L;
        String grade = "BASIC";
        String invalidPublicId = "res-123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.createReservation(userId, grade, invalidPublicId, null)
        ).isInstanceOf(InvalidSchedulePublicId.class);
    }

    @DisplayName("createReservation: userGrade가 Grade에 없으면 InvalidUserGrade 예외를 발생시킨다.")
    @Test
    void createReservation_userGrade가_Grade_type으로_변환_불가시_예외() {
        // given
        Long userId = 1L;
        String grade = "HELLO";
        String invalidPublicId = "sch-123123";

        // when, then
        assertThatThrownBy(() ->
                reservationPopupService.createReservation(userId, grade, invalidPublicId, null)
        ).isInstanceOf(InvalidUserGrade.class);
    }

    @DisplayName("createReservation: BASIC 회원 정상 로직 - Response Body/ Reservation 검증")
    @Test
    void createReservation_BASIC_Reservation_검증() {
        Long userId = 1L;
        String grade = "BASIC";
        String publicId = "sch-001";

        User user = User.create("김지오", "geo", User.Grade.BASIC, "01012341234");
        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");

        Schedule schedule = Schedule.create(
                LocalDateTime.of(2025, 11, 5, 0, 0),
                5,
                3,
                Schedule.Status.WAITING,
                Schedule.Type.NORMAL,
                ship
        );

        Coupon coupon = Coupon.create(Coupon.Type.WEEKDAY, user);
        ReflectionTestUtils.setField(coupon, "id", 10L);
        Long couponId = coupon.getId();

        // repository mock stubbing
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        ReservationPopupRequest request = ReservationPopupRequest.builder()
                .headCount(2)
                .request("내가 왕이다.")
                .couponId(coupon.getId())
                .build();

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(userId, grade, publicId, request);

        verify(reservationRepository).save(captor.capture());
        Reservation saved = captor.getValue();

        //common
        assertThat(reservationCreateResponse).isNotNull();
        //Reservation
        assertThat(saved.getPublicId()).isEqualTo(reservationCreateResponse.getReservationPublicId());
        assertThat(saved.getHeadCount()).isEqualTo(2);
        assertThat(saved.getRequest()).isEqualTo("내가 왕이다.");
        assertThat(saved.getTotalPrice()).isEqualTo(2 * ship.getPrice());
        assertThat(saved.getProcess()).isEqualTo(Reservation.Process.RESERVE_COMPLETED);
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getSchedule()).isEqualTo(schedule);
        assertThat(saved.getCoupon()).isEqualTo(coupon);
    }

    @DisplayName("createReservation: BASIC 회원 정상 로직 - Schedule 검증")
    @Test
    void createReservation_BASIC_Schedule_검증() {
        Long userId = 1L;
        String grade = "BASIC";
        String publicId = "sch-001";

        User user = User.create("김지오", "geo", User.Grade.BASIC, "01012341234");
        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");

        Schedule schedule = Schedule.create(
                LocalDateTime.of(2025, 11, 5, 0, 0),
                5,
                3,
                Schedule.Status.WAITING,
                Schedule.Type.NORMAL,
                ship
        );
        int beforeCurrentHeadCount = schedule.getCurrentHeadCount();

        Coupon coupon = Coupon.create(Coupon.Type.WEEKDAY, user);

        // repository mock stubbing
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationPopupRequest request = ReservationPopupRequest.builder()
                .headCount(2)
                .request("내가 왕이다.")
                .couponId(coupon.getId())
                .build();


        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(userId, grade, publicId, request);

        int afterCurrentHeadCount = schedule.getCurrentHeadCount();
       //Schedule
        assertThat(afterCurrentHeadCount).isEqualTo(beforeCurrentHeadCount - request.getHeadCount());
    }

    @DisplayName("createReservation: BASIC 회원 정상 로직 - Coupon 검증")
    @Test
    void createReservation_BASIC_Coupon_검증() {
        Long userId = 1L;
        String grade = "BASIC";
        String publicId = "sch-001";

        User user = User.create("김지오", "geo", User.Grade.BASIC, "01012341234");
        Ship ship = Ship.create(20, "쭈갑", 90000, "주의사항 없음");

        Schedule schedule = Schedule.create(
                LocalDateTime.of(2025, 11, 5, 0, 0),
                5,
                3,
                Schedule.Status.WAITING,
                Schedule.Type.NORMAL,
                ship
        );

        Coupon coupon = Coupon.create(Coupon.Type.WEEKDAY, user);
        //coupon id 강제 세팅
        ReflectionTestUtils.setField(coupon, "id", 1L);

        ReservationPopupRequest request = ReservationPopupRequest.builder()
                .headCount(2)
                .request("내가 왕이다.")
                .couponId(coupon.getId())
                .build();

        // repository mock stubbing
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(scheduleRepository.findByPublicId(publicId)).thenReturn(Optional.of(schedule));
        when(couponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationCreateResponse reservationCreateResponse = reservationPopupService.createReservation(userId, grade, publicId, request);

        //Coupon
        assertThat(coupon.getUser()).isEqualTo(user);
        assertThat(coupon.getStatus()).isEqualTo(Coupon.Status.USED);
    }

}