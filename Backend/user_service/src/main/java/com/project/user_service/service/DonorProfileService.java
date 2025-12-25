package com.project.user_service.service;

import com.project.user_service.dto.donor.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DonorProfileService {
    DonorProfileResponseDto createDonorProfile(UUID userId, DonorProfileCreateDto createDto);
    DonorProfileResponseDto getDonorProfile(UUID userId);
    DonorProfileResponseDto updateDonorProfile(UUID userId, DonorProfileUpdateDto updateDto);
    DonorProfileResponseDto verifyDonor(UUID userId, DonorVerificationDto verificationDto);
    void deleteDonorProfile(UUID userId);
    Page<DonorProfileResponseDto> searchDonors(DonorSearchCriteriaDto criteria, Pageable pageable);
}
