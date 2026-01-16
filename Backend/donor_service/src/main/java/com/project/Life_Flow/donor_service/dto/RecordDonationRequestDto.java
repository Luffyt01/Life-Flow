package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDonationRequestDto {
    private UUID donorId;
    private UUID appointmentSlotId;
    private BloodType bloodType;
    private BigDecimal units;
    private Object healthMetrics; // Placeholder
    private String staffNotes;
}
