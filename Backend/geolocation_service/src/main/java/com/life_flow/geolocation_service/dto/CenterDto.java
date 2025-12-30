package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class CenterDto {
    private String centerId;
    private String name;
    private double latitude;
    private double longitude;
}
