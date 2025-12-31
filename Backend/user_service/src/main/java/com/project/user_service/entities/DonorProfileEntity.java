package com.project.user_service.entities;

import com.project.user_service.entities.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donor_profiles",
        indexes = {
                @Index(name = "idx_donor_blood_type", columnList = "bloodType"),
                @Index(name = "idx_donor_eligibility", columnList = "eligibilityStatus"),
                @Index(name = "idx_donor_last_donation", columnList = "lastDonationDate"),
                @Index(name = "idx_donor_location", columnList = "location")
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

    private String address;


    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private BigDecimal weightKg;

    private Integer heightCm;

    private BigDecimal bmi;

    private BigDecimal hemoglobinLevel;

    private LocalDate lastDonationDate;


    @Enumerated(EnumType.STRING)
    private EligibilityStatus eligibilityStatus;

    private LocalDate eligibilityExpiryDate;

    private String medicalConditions;
    @Column(length = 500)
    private String medications;

    @Column(length = 500)
    private String allergies;

    private LocalDate tattooDate;
    @Column(length = 500)
    private String recentTravel;

    @Enumerated(EnumType.STRING)
    private VaccinationStatus vaccinationStatus;

    @Column(length = 500)
    private String blockReason;

    private LocalDateTime blockUntil;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    private String verifiedByAdmin;

    private LocalDateTime verifiedAt;

    private LocalDateTime lastEligibilityCheck;

    private UUID preferredCenterId;

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
