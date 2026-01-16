package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import com.project.Life_Flow.donor_service.entities.enums.BloodType; //
import com.project.Life_Flow.donor_service.entities.enums.EligibilityStatus; //
import com.project.Life_Flow.donor_service.entities.enums.VerificationStatus; //
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DonorProfileResponse {
    private UUID donorId;
    private UUID userId;
    private BloodType bloodType;
    private Integer ageYears;
    private BigDecimal weightKg;
    private Integer heightCm;
    private BigDecimal bmi;
    private EligibilityStatus eligibilityStatus;
    private VerificationStatus verificationStatus;
    private LocalDateTime lastEligibilityCheck;

    // Gamification Summary
    private BadgeLevel badgeLevel;
    private Integer totalPoints;
}
