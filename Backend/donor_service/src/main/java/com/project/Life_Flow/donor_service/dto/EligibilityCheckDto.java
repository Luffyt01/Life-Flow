package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonorProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EligibilityCheckDto {
    private UUID checkId;
    private UUID donorId;
    private LocalDateTime checkTimestamp;
    private Integer lastDonationDays;
    private BigDecimal minWeightKg;
    private BigDecimal minHemoglobin;
    private Integer ageYears;
    private Boolean medicalConditionsClear;
    private Boolean medicationsAllowed;
    private Boolean travelRisk;
    private Boolean tattooRisk;
    private Boolean vaccinationRisk;
    private Boolean overallEligible;
    private String reasonIfIneligible;
    private LocalDateTime recheckedBySystem;
}
