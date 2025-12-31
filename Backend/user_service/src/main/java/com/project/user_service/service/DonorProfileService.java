package com.project.user_service.service;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.dto.donor.*;
import com.project.user_service.entities.enums.BloodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface DonorProfileService {
    DonorProfileResponseDto createDonorProfile(UUID userId, DonorProfileCreateDto createDto);
    DonorProfileResponseDto getDonorProfile(UUID userId);
    DonorProfileResponseDto updateDonorProfile(UUID userId, DonorProfileUpdateDto updateDto);
    DonorProfileResponseDto verifyDonor(UUID userId, DonorVerificationDto verificationDto);
    void deleteDonorProfile(UUID userId);
    Page<DonorProfileResponseDto> searchDonors(DonorSearchCriteriaDto criteria, Pageable pageable);
    void updateDonorLocation(UUID userId, PointDTO locationDto);
    List<DonorProfileResponseDto> findNearbyDonors(Double latitude, Double longitude, Double radiusKm, BloodType bloodType);

    List<DonorProfileResponseLessDto> findNearbyDonorsWithLessResp(Double latitude, Double longitude, Double radiusKm, BloodType bloodType);
}
