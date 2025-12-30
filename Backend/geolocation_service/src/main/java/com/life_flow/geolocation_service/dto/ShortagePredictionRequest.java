package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class ShortagePredictionRequest {
    private String center_id;
    private String blood_type;
    private int forecast_days;
    private double confidence_level;
}
