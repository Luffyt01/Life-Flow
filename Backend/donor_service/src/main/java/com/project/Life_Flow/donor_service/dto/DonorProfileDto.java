package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.EligibilityStatus;
import com.project.Life_Flow.donor_service.entities.enums.Gender;
import com.project.Life_Flow.donor_service.entities.enums.VerificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileDto {

    private UUID donorId;
    private UUID userId;
    private BloodType bloodType;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BigDecimal weightKg;
    private Integer heightCm;
    private BigDecimal bmi;
    private BigDecimal hemoglobinLevel;
    private String chronicDiseases;
    private String allergies;
    private String medications;
    private LocalDate tattooDate;
    private String recentTravel;
    private String vaccinationStatus;
    private EligibilityStatus eligibilityStatus = EligibilityStatus.PENDING_VERIFICATION;
    private String blockReason;
    private LocalDateTime blockUntil;
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;
    private String verifiedByAdmin;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastEligibilityCheck;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
