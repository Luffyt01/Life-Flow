package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class NearbyDonorsResponse {
    private List<DonorDto> donors;
    private int total_count;
    private String estimated_completion_time;
    private String coverage_map;
}
