package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ResponseTimeStatsResponse {
    private double average_response_time;
    private double percentile_90;
    private String best_time_of_day;
    private Map<String, Object> seasonal_trends;
}
