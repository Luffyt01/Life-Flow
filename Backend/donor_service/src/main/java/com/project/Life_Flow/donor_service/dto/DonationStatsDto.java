package com.project.Life_Flow.donor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationStatsDto {
    private long totalDonations;
    private BigDecimal totalUnits;
    private double avgFrequencyDays;
    private LocalDate lastDonationDate;
    private Object healthTrends; // Placeholder
}
