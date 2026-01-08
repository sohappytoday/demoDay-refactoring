package com.lamarfishing.core.user.dto.command;

import com.lamarfishing.core.user.domain.Grade;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NormalReservationUserDto {

    private String username;
    private String nickname;
    private Grade grade;
    private String phone;

}