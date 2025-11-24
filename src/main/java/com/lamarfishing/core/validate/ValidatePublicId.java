package com.lamarfishing.core.validate;


import com.lamarfishing.core.reservation.exception.InvalidReservationPublicId;

public class ValidatePublicId {

    public static void validateReservationPublicId(String publicId){
        if(!publicId.startsWith("res")) {
            throw new InvalidReservationPublicId();
        }
    }

    public static void validateSchedulePublicId(String publicId){
        if(!publicId.startsWith("sch")) {
            throw new InvalidReservationPublicId();
        }
    }
}
