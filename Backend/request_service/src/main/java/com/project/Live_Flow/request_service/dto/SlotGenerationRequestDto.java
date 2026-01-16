package com.project.Live_Flow.request_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotGenerationRequestDto {

    @NotNull(message = "Slot duration cannot be null")
    @Min(value = 1, message = "Slot duration must be at least 1 minute")
    private Integer slotDuration;

    @NotNull(message = "Max donors cannot be null")
    @Min(value = 1, message = "Max donors must be at least 1")
    private Integer maxDonors;

    // In a real scenario, this would likely be a more complex object
    private String priorityCriteria;
}
