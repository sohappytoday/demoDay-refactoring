package com.lamarfishing.core.ship.service;

import com.lamarfishing.core.common.dto.response.PageResponse;
import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.request.DeleteShipRequest;
import com.lamarfishing.core.ship.dto.request.UpdateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipDetailResponse;
import com.lamarfishing.core.ship.exception.ShipNotFound;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipService {

    private final ShipRepository shipRepository;
    private final UserRepository userRepository;

    public PageResponse<ShipDetailResponse> getShips(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (user.getGrade() != User.Grade.ADMIN) {
            throw new InvalidUserGrade();
        }

        Page<ShipDetailResponse> ships = shipRepository.findAll(pageable)
                .map(ShipMapper::toShipDetailDto)
                .map(ShipDetailResponse::from);

        return PageResponse.from(ships);
    }

    @Transactional
    public void createShip(Long userId, CreateShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (user.getGrade() != User.Grade.ADMIN) {
            throw new InvalidUserGrade();
        }

        String fishType = request.getFishType();
        int price = request.getPrice();
        int maxHeadCount = request.getMaxHeadCount();
        String notification = request.getNotification();

        Ship ship = Ship.create(maxHeadCount, fishType, price, notification); // 객체 메서드를 객체에 받는 코드
        shipRepository.save(ship);  // DB에 저장

    }

    @Transactional
    public void updateShip(Long userId, Long shipId, UpdateShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (user.getGrade() != User.Grade.ADMIN) {
            throw new InvalidUserGrade();
        }

        Ship ship = shipRepository.findById(shipId).orElseThrow(ShipNotFound::new);

        if (request.getFishType() != null){
            ship.updateFishType(request.getFishType());
        }
        if(request.getPrice() != null){
            ship.updatePrice(request.getPrice());
        }
       if(request.getMaxHeadCount() != null){
           ship.updateMaxHeadCount(request.getMaxHeadCount());
       }
       if(request.getNotification() !=null){
           ship.updateNotification(request.getNotification());
       }
    }

    @Transactional
    public void deleteShip(Long userId, DeleteShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if (user.getGrade() != User.Grade.ADMIN) {
            throw new InvalidUserGrade();
        }

        List<Long> shipIds = request.getShipIds();
        List<Ship> ships = shipRepository.findAllById(shipIds);

        if (ships.size() != shipIds.size()) {
            throw new ShipNotFound();
        }
        shipRepository.deleteAllInBatch(ships);
    }
}
