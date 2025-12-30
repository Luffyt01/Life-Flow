package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class OptimizeRoutesResponse {
    private List<String> optimized_routes;
    private double total_distance;
    private double total_time;
    private String route_map_url;
}
