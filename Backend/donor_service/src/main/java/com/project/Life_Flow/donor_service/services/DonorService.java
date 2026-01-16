package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileResponse;
import com.project.Life_Flow.donor_service.dto.EligibilityCheckResponse;
import com.project.Life_Flow.donor_service.entities.DonorProfile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface DonorService {

    DonorProfileResponse getMyProfile(UUID userId);

    DonorProfileResponse registerDonor(DonorProfileRequestDto request, UUID userId);

    DonorProfileResponse getDonorProfile(UUID donorId);

    EligibilityCheckResponse checkEligibility(UUID donorId);

    DonorProfile getDonorEntity(UUID donorId);
}
