package com.Life_flow.hospital_service.dto;

import com.Life_flow.hospital_service.entity.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BloodRequestDto {
    private UUID requestId;

    @NotNull(message = "Hospital ID is required")
    private UUID hospitalId;

    private String hospitalName;
    private String hospitalAddress;
    private Double hospitalLatitude;
    private Double hospitalLongitude;

    @NotNull(message = "Blood type is required")
    private BloodType bloodTypeNeeded;

    @NotNull(message = "Units required is required")
    private Double unitsRequired;

    private UrgencyLevel urgencyLevel = UrgencyLevel.MEDIUM;
    private Integer patientAge;
    private Gender patientGender;
    private String patientCondition;
    private String procedureType;

    @NotNull(message = "Required by date is required")
    private LocalDateTime requiredBy;

    private RequestStatus status = RequestStatus.PENDING;
    private Integer searchRadiusKm = 50;
    private String preferredDonationCenters;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}