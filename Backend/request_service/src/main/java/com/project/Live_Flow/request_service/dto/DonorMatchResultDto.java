package com.project.Live_Flow.request_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorMatchResultDto {
    private UUID donorId;
    private BigDecimal matchScore;
    private BigDecimal distanceKm;
}
