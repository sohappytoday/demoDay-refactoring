package com.lamarfishing.core.ship.service.query;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import com.lamarfishing.core.ship.repository.ShipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ShipQueryServiceTest {

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private ShipQueryService shipQueryService;

    @Test
    @DisplayName("Ship 전체 조회 성공")
    void getShips_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Ship ship1 = mock(Ship.class);
        Ship ship2 = mock(Ship.class);

        List<Ship> shipList = List.of(ship1, ship2);
        Page<Ship> shipPage = new PageImpl<>(shipList, pageable, shipList.size());

        when(shipRepository.findAll(pageable)).thenReturn(shipPage);

        // when
        Page<ShipDetailDto> result = shipQueryService.getShips(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(2);

        verify(shipRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Ship 이 없는 경우 빈 페이지를 반환한다")
    void getShips_empty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ship> emptyPage = Page.empty(pageable);

        when(shipRepository.findAll(pageable)).thenReturn(emptyPage);

        // when
        Page<ShipDetailDto> result = shipQueryService.getShips(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();

        verify(shipRepository, times(1)).findAll(pageable);
    }
}