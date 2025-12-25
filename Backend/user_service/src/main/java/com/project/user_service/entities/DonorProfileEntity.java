package com.project.user_service.entities;

import com.project.user_service.entities.enums.BloodType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.project.user_service.entities.enums.EligibilityStatus;
import com.project.user_service.entities.enums.Gender;
import com.project.user_service.entities.enums.VaccinationStatus;
import com.project.user_service.entities.enums.VerificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "donor_profiles",
        indexes = {
                @Index(name = "idx_donor_blood_type", columnList = "bloodType"),
                @Index(name = "idx_donor_eligibility", columnList = "eligibilityStatus"),
                @Index(name = "idx_donor_last_donation", columnList = "lastDonationDate")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorProfileEntity {

    @Id
    private UUID donorId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "donor_id")
    private UserEntity user;

    @Column(nullable = false, length = 5)
    private BloodType bloodType;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(precision = 5, scale = 2)
    private BigDecimal weightKg;

    private Integer heightCm;

    @Column(precision = 5, scale = 2)
    private BigDecimal bmi;

    @Column(precision = 4, scale = 1)
    private BigDecimal hemoglobinLevel;

    private LocalDate lastDonationDate;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private EligibilityStatus eligibilityStatus;

    private LocalDate eligibilityExpiryDate;


    private String medicalConditions;


    private String medications;


    private String allergies;

    private LocalDate tattooDate;



    private String recentTravel;

    @Enumerated(EnumType.STRING)
    private VaccinationStatus vaccinationStatus;

    @Column(length = 500)
    private String blockReason;

    private LocalDateTime blockUntil;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

//    private String verifiedByAdmin;

    private LocalDateTime verifiedAt;

    private LocalDateTime lastEligibilityCheck;

//    private UUID preferredCenterId;

    private Integer averageResponseTimeMinutes;

    private Integer totalDonations;

    @Column(columnDefinition= "Geometry(Point, 4326)")
    private Point location;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        if (eligibilityStatus == null) {
            eligibilityStatus = EligibilityStatus.PENDING_VERIFICATION;
        }
        if (verificationStatus == null) {
            verificationStatus = VerificationStatus.UNVERIFIED;
        }
        if (totalDonations == null) {
            totalDonations = 0;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (lastUpdated == null) {
            lastUpdated = LocalDateTime.now();
        }
    }
}
