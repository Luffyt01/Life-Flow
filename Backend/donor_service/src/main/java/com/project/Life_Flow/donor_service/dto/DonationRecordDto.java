package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationRecordDto {
    private UUID donationId;
    private LocalDateTime date;
    private BloodType bloodType;
    private BigDecimal units;
    private Integer healthScore;
    private String centerName; // Assuming we can get this from the center_id
    private DonationStatus status;
}
