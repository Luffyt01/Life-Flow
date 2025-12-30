package com.project.user_service.dto.donor;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.entities.enums.Gender;
import com.project.user_service.entities.enums.VaccinationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileUpdateDto {
    private BigDecimal weightKg;
    private Integer heightCm;
    private String medicalConditions;
    private String medications;
    private String allergies;
    private LocalDate tattooDate;
    private String recentTravel;
    private VaccinationStatus vaccinationStatus;
//    private UUID preferredCenterId;
    private LocalDate lastDonationDate;
    private PointDTO location;
}
