package com.lamarfishing.core.ship.service;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
import com.lamarfishing.core.ship.dto.request.CreateShipRequest;
import com.lamarfishing.core.ship.dto.response.ShipListResponse;
import com.lamarfishing.core.ship.mapper.ShipMapper;
import com.lamarfishing.core.ship.repository.ShipRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipService {

    private final ShipRepository shipRepository;
    private final UserRepository userRepository;

    public ShipListResponse getShips(Long userId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if(user.getGrade() != User.Grade.ADMIN){
            throw new InvalidUserGrade();
        }

        List<ShipDetailDto> shipDtos = shipRepository.findAll()
                .stream()
                .map(ShipMapper::toShipDetailDto)
                .toList();

        return ShipListResponse.from(shipDtos);
    }

    @Transactional
    public void CreateShip(Long userId, CreateShipRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);

        if(user.getGrade() != User.Grade.ADMIN){
            throw new InvalidUserGrade();
        }

        String fishType = request.getFishType();
        int price = request.getPrice();
        int maxHeadCount = request.getMaxHeadCount();
        String notification = request.getNotification();

        Ship ship = Ship.create(maxHeadCount, fishType, price, notification); // 객체 메서드를 객체에 받는 코드
        shipRepository.save(ship);  // DB에 저장

    }
}
