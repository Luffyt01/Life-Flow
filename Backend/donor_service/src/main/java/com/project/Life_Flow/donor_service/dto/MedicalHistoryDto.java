package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.enums.MedicalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryDto {

    private UUID historyId;
    private UUID donor;
    private String conditionName;
    private LocalDate diagnosisDate;
    private MedicalStatus status;
    private String treatmentDetails;
    private LocalDate lastTreatmentDate;
    private LocalDateTime createdAt;
}
