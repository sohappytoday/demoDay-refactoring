package com.lamarfishing.core.reservation.service;

import com.lamarfishing.core.reservation.domain.Reservation;
import com.lamarfishing.core.reservation.dto.response.ReservationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    public ReservationDetailResponse  getReservationDetail(Long userId, String publicId){
        if(!publicId.startsWith("res")){
            throw new
        }
    }
}
