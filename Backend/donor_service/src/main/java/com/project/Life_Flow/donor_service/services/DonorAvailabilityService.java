package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;

import java.util.List;
import java.util.UUID;

public interface DonorAvailabilityService {
    UpdateAvailabilityResponseDto updateDonorAvailability(UUID donorId, DonorAvailabilityDto availabilityDto);
    List<DonorAvailabilityDto> getDonorAvailability(UUID donorId);
    CurrentAvailabilityDto getCurrentDonorAvailability(UUID donorId);
    BulkAvailabilityUpdateResponseDto bulkUpdateDonorAvailability(UUID donorId, BulkAvailabilityUpdateRequestDto requestDto);
    void updateEmergencyAvailability(UUID donorId, EmergencyAvailabilityDto emergencyAvailabilityDto);
    void deleteDonorAvailability(UUID donorId, String day);
}
