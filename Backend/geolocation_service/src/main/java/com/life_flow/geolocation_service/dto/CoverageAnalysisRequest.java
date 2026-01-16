package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class CoverageAnalysisRequest {
    private String center_id;
    private String time_of_day;
    private String day_of_week;
    private String blood_type;
}
