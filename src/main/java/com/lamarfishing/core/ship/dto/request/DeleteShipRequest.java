package com.lamarfishing.core.ship.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeleteShipRequest {
    private List<Long> shipIds;
}
