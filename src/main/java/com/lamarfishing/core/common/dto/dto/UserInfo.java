package com.lamarfishing.core.common.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class UserInfo {

    String sub;
    String provider;
}
