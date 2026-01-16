package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class CoverageReportResponse {
    private String report_url;
    private double coverage_score;
    private List<String> recommendations;
    private String generated_at;
}
