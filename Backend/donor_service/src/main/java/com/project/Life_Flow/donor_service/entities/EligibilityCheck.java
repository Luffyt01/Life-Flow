package com.project.Life_Flow.donor_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eligibility_checks",
        indexes = {
                @Index(name = "idx_donor_id", columnList = "donor_id"),
                @Index(name = "idx_check_timestamp", columnList = "checkTimestamp")
        })
public class EligibilityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String checkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    @CreationTimestamp
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
