package com.project.user_service.dto.donor;

import com.project.user_service.entities.enums.BloodType;
import com.project.user_service.entities.enums.EligibilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorSearchCriteriaDto {
    private BloodType bloodType;
    private String city;
    private BigDecimal minWeight;
    private EligibilityStatus eligibilityStatus;
    private Double latitude;
    private Double longitude;
    private Double radiusKm;
}
