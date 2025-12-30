package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CalculateSlotsResponse {
    private List<String> optimal_slots;
    private double efficiency_score;
    private Map<String, String> donor_assignments;
    private Map<String, Object> gap_analysis;
}
