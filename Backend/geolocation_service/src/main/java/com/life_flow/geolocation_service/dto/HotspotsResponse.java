package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class HotspotsResponse {
    private List<String> hotspots;
    private double donor_concentration;
    private double utilization_rate;
    private List<String> expansion_recommendations;
}
