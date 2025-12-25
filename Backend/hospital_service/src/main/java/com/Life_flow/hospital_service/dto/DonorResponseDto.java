package com.Life_flow.hospital_service.dto;


import com.Life_flow.hospital_service.entity.enums.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DonorResponseDto {
    private UUID responseId;
    private UUID requestId;
    private UUID donorId;
    private String donorName;
    private BloodType donorBloodType;
    private String donorPhone;
    private Double donorLatitude;
    private Double donorLongitude;

    private ResponseStatus responseStatus = ResponseStatus.PENDING;
    private Double distanceToHospitalKm;
    private Integer estimatedTravelTimeMinutes;
    private Integer canDonateAfterDays;
    private UUID preferredDonationCenterId;
    private LocalDateTime preferredDonationDate;
    private String additionalNotes;
    private String rejectionReason;
    private Integer matchScore;

    private LocalDateTime respondedAt;
}