package com.project.Life_Flow.donor_service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EligibilityCheckResponse {

    private UUID checkId;
    private LocalDateTime checkTimestamp;
    private Boolean overallEligible;
    private String reasonIfIneligible;
    private Integer lastDonationDays;
    private Boolean medicalConditionsClear;
    private Boolean travelRisk;
    private Boolean vaccinationRisk;
}
