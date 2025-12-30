package com.life_flow.geolocation_service.dto.user_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDTO {
    private double[] coordinates;
    private String type = "Point";
}
