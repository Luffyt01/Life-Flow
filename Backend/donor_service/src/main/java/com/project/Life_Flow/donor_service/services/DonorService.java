package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.entities.DonorProfile;

import java.util.UUID;

public interface DonorService {

    DonorProfileDto getMyProfile(UUID donorId);

    DonorProfileDto createDonorProfile(DonorProfileRequestDto donorProfileRequestDto);
}
