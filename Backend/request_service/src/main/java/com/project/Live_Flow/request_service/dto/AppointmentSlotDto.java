package com.project.Live_Flow.request_service.dto;

import com.project.Live_Flow.request_service.entities.enums.SlotStatus;
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
public class AppointmentSlotDto {
    private UUID slotId;
    private UUID requestId;
    private UUID donorId;
    private LocalDateTime appointmentTime;
    private SlotStatus status;
}
