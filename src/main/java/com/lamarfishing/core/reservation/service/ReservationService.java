package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;
import com.lamarfishing.core.reservation.repository.ReservationRepository;
import com.lamarfishing.core.user.domain.User;
import com.lamarfishing.core.user.exception.UserNotFound;
import com.lamarfishing.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ReservationDetailResponse  getReservationDetail(Long userId, String publicId){
        if(!publicId.startsWith("res")){
            throw new InvalidReservationPublicId();
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFound::new);
        Reservation reservation = reservationRepository.findByPublicId(publicId).orElseThrow()
        if(!(user.getGrade().equals(User.Grade.ADMIN) || ){

        }
    }
}
