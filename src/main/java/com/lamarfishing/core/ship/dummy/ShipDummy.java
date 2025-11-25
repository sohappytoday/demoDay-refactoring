package com.lamarfishing.core.ship.dummy;

import com.lamarfishing.core.ship.domain.Ship;
import com.lamarfishing.core.ship.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ShipDummy {

    private final ShipRepository shipRepository;

    public void init() {

        if (shipRepository.count() > 0) {
            return;
        }

        shipRepository.save(Ship.create(20, "쭈갑", 90000, "공지사항입니다1"));
        shipRepository.save(Ship.create(15, "갑오징어", 80000, "공지사항입니다2"));
        shipRepository.save(Ship.create(20, "쭈꾸미", 100000, "공지사항입니다3"));
        shipRepository.save(Ship.create(15, "쭈갑", 90000, "공지사항입니다4"));
        shipRepository.save(Ship.create(20, "갑오징어", 80000, "공지사항입니다5"));
        shipRepository.save(Ship.create(20, "참동타이라바", 150000, "공지사항입니다6"));
        shipRepository.save(Ship.create(30, "우럭외수질", 100000, "공지사항입니다7"));

    }
}
