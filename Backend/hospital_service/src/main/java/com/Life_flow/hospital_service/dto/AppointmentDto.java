package com.Life_flow.hospital_service.dto;


import com.Life_flow.hospital_service.entity.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDto {
    private UUID appointmentId;
    private UUID requestId;
    private UUID responseId;
    private UUID donationCenterId;
    private String donationCenterName;
    private String donationCenterAddress;

    @NotNull(message = "Scheduled date time is required")
    private LocalDateTime scheduledDateTime;

    private Integer estimatedDurationMinutes = 60;
    private AppointmentStatus appointmentStatus = AppointmentStatus.SCHEDULED;
    private String confirmationCode;
    private LocalDateTime donorArrivedAt;
    private LocalDateTime donationStartedAt;
    private LocalDateTime donationCompletedAt;
    private UUID bloodBagId;
    private String cancellationReason;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}