package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class ShortagePredictionResponse {
    private String predicted_shortage_date;
    private int shortage_units;
    private String confidence_interval;
    private List<String> mitigation_suggestions;
}
