package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.enums.AdverseEvents;
import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.DonationStatus;
import com.project.Life_Flow.donor_service.entities.enums.DonationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorHistoryDto {

    private UUID donationId;
    private UUID donor;
    private LocalDate donationDate;
    private BloodType bloodTypeCollected;
    private BigDecimal unitsCollected;
    private DonationType donationType;
    private Integer healthScoreBefore;
    private Integer healthScoreAfter;
    private BigDecimal hemoglobinBefore;
    private BigDecimal hemoglobinAfter;
    private String collectionCenterId;
    private String collectionStaffId;
    private String staffNotes;
    private AdverseEvents adverseEvents;
    private DonationStatus donationStatus;
    private String bagBarcode;
    private LocalDateTime createdAt;
}
