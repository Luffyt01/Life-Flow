package com.project.Live_Flow.request_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequestDto {

    @NotNull(message = "Donation successful flag cannot be null")
    private Boolean donationSuccessful;

    private HealthMetricsDto healthMetrics;
    private String staffNotes;
}
