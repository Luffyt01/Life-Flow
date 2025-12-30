package com.project.user_service.dto.donor;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.entities.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileDto {
    private UUID donorId;
    private BloodType bloodType;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;

    private BigDecimal weightKg;
    private Integer heightCm;
    private BigDecimal bmi;
    private BigDecimal hemoglobinLevel;
    private LocalDate lastDonationDate;
    private EligibilityStatus eligibilityStatus;
    private LocalDate eligibilityExpiryDate;
    private String medicalConditions;
    private String medications;
    private String allergies;
    private LocalDate tattooDate;
    private String recentTravel;
    private VaccinationStatus vaccinationStatus;
    private String blockReason;
    private LocalDateTime blockUntil;
    private VerificationStatus verificationStatus;
//    private String verifiedByAdmin;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastEligibilityCheck;
    private Integer totalDonations;
    private PointDTO location;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
