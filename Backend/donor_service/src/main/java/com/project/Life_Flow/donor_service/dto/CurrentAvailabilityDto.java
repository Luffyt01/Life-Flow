package com.project.Life_Flow.donor_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentAvailabilityDto {
    private boolean isAvailableNow;
    private LocalDateTime nextAvailableTime;
    private String locationStatus;
}
