package com.project.Live_Flow.request_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricsDto {
    private Integer heartRate;
    private String bloodPressure;
    private Float temperature;
}
