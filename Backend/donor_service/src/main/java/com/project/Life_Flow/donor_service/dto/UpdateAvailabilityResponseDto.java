package com.project.Life_Flow.donor_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAvailabilityResponseDto {
    private UUID availabilityId;
    private LocalDateTime updatedAt;
}
