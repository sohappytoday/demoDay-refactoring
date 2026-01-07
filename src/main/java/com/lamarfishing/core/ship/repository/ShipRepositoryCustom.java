package com.lamarfishing.core.ship.repository;

import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipRepositoryCustom {
    Page<ShipDetailDto> getShips(Pageable pageable);
}
