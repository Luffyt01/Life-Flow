package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.dto.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorAvailabilityDto {

    @NotNull
    private DayOfWeek dayOfWeek;

    private List<TimeSlotDto> timeSlots;

    private Integer preferredRadiusKm;

    private Boolean isAvailableEmergency;
}
