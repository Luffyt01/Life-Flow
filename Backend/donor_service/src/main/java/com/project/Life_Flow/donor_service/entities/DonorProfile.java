package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.EligibilityStatus;
import com.project.Life_Flow.donor_service.entities.enums.Gender;
import com.project.Life_Flow.donor_service.entities.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "donor_profiles",
        indexes = {
                @Index(name = "idx_user_id", columnList = "userId"),
                @Index(name = "idx_blood_type", columnList = "bloodType"),
                @Index(name = "idx_eligibility_status", columnList = "eligibilityStatus"),
                @Index(name = "idx_block_until", columnList = "blockUntil")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String donorId;

    @Column(nullable = false, length = 50)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodType bloodType;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private BigDecimal weightKg;
    private Integer heightCm;
    private BigDecimal bmi;
    private BigDecimal hemoglobinLevel;

    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer pulseRate;
    private BigDecimal bodyTemperature;

    @Column(length = 500)
    private String chronicDiseases;

    @Column(length = 500)
    private String allergies;

    @Lob
    private String medications;

    private LocalDate tattooDate;

    @Column(length = 500)
    private String recentTravel;

    @Column(length = 100)
    private String vaccinationStatus;

    @Enumerated(EnumType.STRING)
    private EligibilityStatus eligibilityStatus = EligibilityStatus.PENDING_VERIFICATION;

    @Column(length = 500)
    private String blockReason;

    private LocalDateTime blockUntil;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;

    private String verifiedByAdmin;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastEligibilityCheck;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
