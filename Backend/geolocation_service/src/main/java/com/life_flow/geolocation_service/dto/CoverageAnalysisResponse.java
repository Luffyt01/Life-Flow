package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class CoverageAnalysisResponse {
    private String coverage_map;
    private List<String> hotspots;
    private List<String> gaps;
    private List<String> recommendations;
}
