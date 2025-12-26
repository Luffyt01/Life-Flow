package com.project.Life_Flow.donor_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyAvailabilityDto {
    @NotNull
    private Boolean isAvailableEmergency;
    private Integer priority;
}
