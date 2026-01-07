package com.lamarfishing.core.ship.repository;

import com.lamarfishing.core.ship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long>, ShipRepositoryCustom {
}
