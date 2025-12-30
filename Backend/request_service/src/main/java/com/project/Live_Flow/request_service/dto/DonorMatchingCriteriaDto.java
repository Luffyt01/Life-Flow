package com.project.Live_Flow.request_service.dto;

import com.project.Live_Flow.request_service.entities.enums.BloodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorMatchingCriteriaDto {
    private BloodType bloodType;
    private Double maxDistance;
    private Boolean hasDonatedRecently;
}
