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
public class AddPointsRequestDto {
    @NotNull
    private Integer points;
    private String reason;
    private String referenceId;
    private String referenceType;
}
