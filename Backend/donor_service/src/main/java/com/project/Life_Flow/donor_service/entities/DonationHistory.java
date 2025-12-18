package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.AdverseEvents;
import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.DonationStatus;
import com.project.Life_Flow.donor_service.entities.enums.DonationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "donation_history",
        indexes = {
                @Index(name = "idx_donation_donor_id", columnList = "donor_id"),
                @Index(name = "idx_donation_date", columnList = "donationDate"),
                @Index(name = "idx_blood_type_collected", columnList = "bloodTypeCollected")
        }
)
public class DonationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID donationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    @CreationTimestamp
    private LocalDate donationDate;

    private BloodType bloodTypeCollected;
    private BigDecimal unitsCollected;

    @Enumerated(EnumType.STRING)
    private DonationType donationType;

    private Integer healthScoreBefore;
    private Integer healthScoreAfter;
    private BigDecimal hemoglobinBefore;
    private BigDecimal hemoglobinAfter;
    private String collectionCenterId;
    private String collectionStaffId;

    @Lob
    private String staffNotes;

    @Enumerated(EnumType.STRING)
    private AdverseEvents adverseEvents;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    private String bagBarcode;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
