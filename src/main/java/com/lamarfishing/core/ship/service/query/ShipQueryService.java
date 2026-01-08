package com.lamarfishing.core.ship.service.query;

import com.lamarfishing.core.ship.dto.result.ShipDetailDto;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipQueryService {

    private final ShipRepository shipRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<ShipDetailDto> getShips(Pageable pageable) {
        return shipRepository.getShips(pageable);
    }
}
