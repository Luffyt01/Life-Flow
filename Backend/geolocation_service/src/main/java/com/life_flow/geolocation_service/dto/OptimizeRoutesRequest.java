package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class OptimizeRoutesRequest {
    private String center_id;
    private List<String> appointments;
    private int vehicle_capacity;
    private Map<String, Object> time_constraints;
}
