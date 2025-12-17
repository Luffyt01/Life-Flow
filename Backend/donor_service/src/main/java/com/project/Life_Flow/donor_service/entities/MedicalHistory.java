package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.MedicalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_history", indexes = {
        @Index(name = "idx_donor_id", columnList = "donor_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    private String conditionName;
    private LocalDate diagnosisDate;

    @Enumerated(EnumType.STRING)
    private MedicalStatus status;

    @Lob
    private String treatmentDetails;

    private LocalDate lastTreatmentDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
