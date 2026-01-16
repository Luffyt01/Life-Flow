package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class DonorLocationResponse {
    private double latitude;
    private double longitude;
    private double accuracy_meters;
    private String last_updated;
    private String location_source;
}
