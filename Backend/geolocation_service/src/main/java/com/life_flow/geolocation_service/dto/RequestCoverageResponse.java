package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class RequestCoverageResponse {
    private String coverage_area;
    private double donor_density;
    private String response_heatmap;
    private double success_probability;
}
