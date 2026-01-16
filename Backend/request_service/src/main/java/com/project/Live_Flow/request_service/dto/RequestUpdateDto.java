package com.project.Live_Flow.request_service.dto;

import com.project.Live_Flow.request_service.entities.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDto {
    private UUID requestId;
    private RequestStatus status;
    private String cancellationReason;
}
