package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class NearbyCentersResponse {
    private List<CenterDto> centers;
    private Map<String, Double> distances;
    private Map<String, Boolean> availability;
    private String recommended_center;
}
