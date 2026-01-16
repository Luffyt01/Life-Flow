package com.project.Live_Flow.request_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookAppointmentRequestDto {

    @NotNull(message = "Donor ID cannot be null")
    private UUID donorId;

    @NotNull(message = "Slot ID cannot be null")
    private UUID slotId;

    @NotNull(message = "Request ID cannot be null")
    private UUID requestId;
}
