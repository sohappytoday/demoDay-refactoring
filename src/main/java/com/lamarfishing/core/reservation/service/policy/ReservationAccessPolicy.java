package com.lamarfishing.core.reservation.service.policy;

import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.user.domain.Grade;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.InvalidUserGrade;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationAccessPolicy {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public void validateOwnerOrAdmin(Long reservationId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        if (user.getGrade() == Grade.ADMIN) {
            return;
        }

        if (!reservationRepository.existsByIdAndUserId(reservationId, userId)) {
            throw new InvalidUserGrade();
        }
    }
}
