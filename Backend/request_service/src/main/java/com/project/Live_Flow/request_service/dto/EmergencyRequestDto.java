package com.project.Live_Flow.request_service.dto;

import com.project.Live_Flow.request_service.entities.enums.BloodType;
import com.project.Live_Flow.request_service.entities.enums.UrgencyLevel;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyRequestDto {

    @NotNull(message = "Hospital ID cannot be null")
    private UUID hospitalId;

    @NotNull(message = "Collection Center ID cannot be null")
    private UUID collectionCenterId;

    @NotNull(message = "Blood type needed cannot be null")
    private BloodType bloodTypeNeeded;

    @NotNull(message = "Units required cannot be null")
    @Min(value = 1, message = "Units required must be at least 1")
    private Integer unitsRequired;

    @NotNull(message = "Urgency level cannot be null")
    private UrgencyLevel urgencyLevel;

    @NotNull(message = "Deadline cannot be null")
    @Future(message = "Deadline must be in the future")
    private LocalDateTime deadline;

    private String patientCondition;
    private Integer patientAge;
    private String patientGender;
    private String surgeryType;
    private String doctorName;
    private String wardNumber;
}
