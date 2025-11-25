package com.lamarfishing.core.log.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainInfoDto {

    int depositExpired;
    int deposit24Hour;

}
