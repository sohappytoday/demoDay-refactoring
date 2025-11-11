package com.lamarfishing.core.ship.service;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.dto.command.ShipDetailDto;
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
}
