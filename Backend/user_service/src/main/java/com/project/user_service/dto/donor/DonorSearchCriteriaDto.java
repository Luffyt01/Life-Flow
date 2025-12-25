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
    private String city; // Note: City search might require joining with UserEntity or parsing address if stored there.
    // Assuming for now we might search by location radius or if city is added to profile.
    // If city is not directly in DonorProfile, we might need to search via UserEntity or location coordinates.
    // For this implementation, I will assume we search by other fields first or add city to the entity if needed.
    // Or, if 'city' is part of the User's address, we need a join.
    
    private BigDecimal minWeight;
    private EligibilityStatus eligibilityStatus;
    private Double latitude;
    private Double longitude;
    private Double radiusKm; // For location-based search
}
