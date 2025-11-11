package com.lamarfishing.core.ship.dto.command;

import lombok.Builder;
import lombok.Getter;

/**
 * 출항 일정 상세 조회에 사용되는 dto
 */
@Getter
@Builder
public class ShipDetailDto {

    private Long shipId;
    private String fishType;
    private Integer price;
    private String notification;

}
