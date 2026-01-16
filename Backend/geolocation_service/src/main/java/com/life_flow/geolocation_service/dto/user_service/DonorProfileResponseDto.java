package com.life_flow.geolocation_service.dto.user_service;

import lombok.Data;
import java.util.UUID;

@Data
public class DonorProfileResponseDto {
    private UUID donorId;
    private BloodType bloodType;
    private String address;
    private PointDTO location;
    
    // Helper methods to get lat/long from PointDTO if available
    public Double getLatitude() {
        if (location != null && location.getCoordinates() != null && location.getCoordinates().length >= 2) {
            return location.getCoordinates()[0];
        }
        return null;
    }

    public Double getLongitude() {
        if (location != null && location.getCoordinates() != null && location.getCoordinates().length >= 2) {
            return location.getCoordinates()[1];
        }
        return null;
    }
}
