package com.lamarfishing.core.ship.service;

import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.request.DeleteShipRequest;
import com.lamarfishing.core.ship.dto.request.UpdateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipDetailResponse;
import com.lamarfishing.core.ship.exception.ShipNotFound;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lamarfishing.core.user.domain.Grade.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipService {

    private final ShipRepository shipRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public Page<ShipDetailDto> getShips(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (user.getGrade() != ADMIN) {
            throw new InvalidUserGrade();
        }

        Page<ShipDetailDto> ships = shipRepository.findAll(pageable)
                .map(ShipMapper::toShipDetailDto);

        return ships;
    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void createShip(Long userId, String fishType, int price, int maxHeadCount, String notification) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        Ship ship = Ship.create(maxHeadCount, fishType, price, notification); // 객체 메서드를 객체에 받는 코드
        shipRepository.save(ship);  // DB에 저장

    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void updateShip(Long userId, Long shipId, String fishType, Integer price, Integer maxHeadCount, String notification) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Ship ship = shipRepository.findById(shipId).orElseThrow(ShipNotFound::new);

        if (fishType != null){
            ship.updateFishType(fishType);
        }
        if(price != null){
            ship.updatePrice(price);
        }
       if(maxHeadCount != null){
           ship.updateMaxHeadCount(maxHeadCount);
       }
       if(notification !=null){
           ship.updateNotification(notification);
       }
    }

    @Transactional
    @PreAuthorize("hasAuthority('GRADE_ADMIN')")
    public void deleteShip(Long userId, List<Long> shipIds) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        List<Ship> ships = shipRepository.findAllById(shipIds);

        if (ships.size() != shipIds.size()) {
            throw new ShipNotFound();
        }
        shipRepository.deleteAllInBatch(ships);
    }
}
