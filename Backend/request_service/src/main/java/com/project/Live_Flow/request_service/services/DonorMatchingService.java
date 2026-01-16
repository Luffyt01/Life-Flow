package com.project.Live_Flow.request_service.services;

import com.project.Live_Flow.request_service.dto.DonorMatchResultDto;

import java.util.List;
import java.util.Map;

public interface DonorMatchingService {
    List<DonorMatchResultDto> findDonors(Map<String, Object> payload);
    void notifyDonors(Map<String, Object> payload);
}
