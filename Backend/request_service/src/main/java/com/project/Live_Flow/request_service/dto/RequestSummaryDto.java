package com.project.Live_Flow.request_service.dto;

import com.project.Live_Flow.request_service.entities.enums.RequestStatus;
import com.project.Live_Flow.request_service.entities.enums.UrgencyLevel;
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
public class RequestSummaryDto {
    private UUID requestId;
    private UUID hospitalId;
    private UrgencyLevel urgencyLevel;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private int unitsRequired;
    private int unitsCollected;
}
