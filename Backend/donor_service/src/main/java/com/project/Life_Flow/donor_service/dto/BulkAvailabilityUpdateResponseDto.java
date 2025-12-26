package com.project.Life_Flow.donor_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkAvailabilityUpdateResponseDto {
    private int updatedCount;
}
