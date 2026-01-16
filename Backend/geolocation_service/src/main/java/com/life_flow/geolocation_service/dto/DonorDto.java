package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class DonorDto {
    private String donorId;
    private double latitude;
    private double longitude;
    private String bloodType;
    private double distanceKm;
}
