package com.project.Live_Flow.request_service.services;

import com.project.Live_Flow.request_service.dto.EmergencyRequestDto;
import com.project.Live_Flow.request_service.dto.RequestSummaryDto;
import com.project.Live_Flow.request_service.dto.RequestUpdateDto;

import java.util.List;
import java.util.UUID;

public interface EmergencyRequestService {
    RequestSummaryDto createEmergencyRequest(EmergencyRequestDto requestDto);
    RequestSummaryDto getRequestById(UUID requestId);
    List<RequestSummaryDto> getRequests(UUID hospitalId, String status, String urgency, String dateFrom);
    void cancelRequest(UUID requestId, RequestUpdateDto updateDto);
    void extendRequestDeadline(UUID requestId, RequestUpdateDto updateDto);
    void createBulkRequests(List<EmergencyRequestDto> requests);
}
