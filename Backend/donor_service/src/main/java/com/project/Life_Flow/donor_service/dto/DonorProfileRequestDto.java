package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileRequestDto {

    private BloodType bloodType;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BigDecimal weightKg;
    private Integer heightCm;
    private BigDecimal hemoglobinLevel;
    private String chronicDiseases;
    private String allergies;
    private String medications;
    private LocalDate tattooDate;
    private String recentTravel;
    private String vaccinationStatus;
}
