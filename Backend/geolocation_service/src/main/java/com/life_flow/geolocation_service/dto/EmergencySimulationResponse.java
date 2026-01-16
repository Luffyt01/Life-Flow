package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmergencySimulationResponse {
    private String simulation_result;
    private String estimated_time;
    private double success_probability;
    private List<String> bottlenecks;
}
