package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class TravelTimeResponse {
    private int travel_time_minutes;
    private double distance_km;
    private String optimal_route;
    private String eta;
    private String traffic_conditions;
}
