package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class UpdateLocationRequest {
    private double latitude;
    private double longitude;
    private double accuracy;
    private String timestamp;
    private String source;
}
