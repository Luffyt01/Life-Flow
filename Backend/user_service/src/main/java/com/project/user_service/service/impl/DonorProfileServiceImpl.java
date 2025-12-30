package com.project.user_service.service.impl;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.dto.donor.*;
import com.project.user_service.entities.DonorProfileEntity;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.BloodType;
import com.project.user_service.entities.enums.EligibilityStatus;
import com.project.user_service.entities.enums.VerificationStatus;
import com.project.user_service.repositories.DonorProfileRepository;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.DonorProfileService;
import com.project.user_service.utils.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonorProfileServiceImpl implements DonorProfileService {

    private final DonorProfileRepository donorProfileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    @CachePut(value = "donorProfiles", key = "#userId")
    public DonorProfileResponseDto createDonorProfile(UUID userId, DonorProfileCreateDto createDto) {
        try {
            log.info("Creating donor profile for user ID: {}", userId);
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User not found with ID: {}", userId);
                        return new RuntimeException("User not found");
                    });

            if (donorProfileRepository.existsById(userId)) {
                log.warn("Donor profile already exists for user ID: {}", userId);
                throw new RuntimeException("Donor profile already exists");
            }

            DonorProfileEntity donorProfile = modelMapper.map(createDto, DonorProfileEntity.class);
            donorProfile.setUser(user);

            // Set default values
            donorProfile.setEligibilityStatus(EligibilityStatus.PENDING_VERIFICATION);
            donorProfile.setVerificationStatus(VerificationStatus.UNVERIFIED);

            DonorProfileEntity savedProfile = donorProfileRepository.save(donorProfile);
            log.info("Donor profile created successfully for user ID: {}", userId);
            return modelMapper.map(savedProfile, DonorProfileResponseDto.class);
        }
        catch (Exception e) {
            log.error("Error creating donor profile for user ID: {}", userId, e);
            throw new RuntimeException("Error creating donor profile", e);
        }
    }

    @Override
    @Cacheable(value = "donorProfiles", key = "#userId")
    public DonorProfileResponseDto getDonorProfile(UUID userId) {
        log.info("Fetching donor profile for user ID: {}", userId);
        DonorProfileEntity donorProfile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Donor profile not found for user ID: {}", userId);
                    return new RuntimeException("Donor profile not found");
                });
        return modelMapper.map(donorProfile, DonorProfileResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(value = "donorProfiles", key = "#userId")
    public DonorProfileResponseDto updateDonorProfile(UUID userId, DonorProfileUpdateDto updateDto) {
        log.info("Updating donor profile for user ID: {}", userId);
        DonorProfileEntity donorProfile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Donor profile not found for user ID: {}", userId);
                    return new RuntimeException("Donor profile not found");
                });

        // Map non-null values from DTO to Entity
        modelMapper.map(updateDto, donorProfile);

        // Recalculate BMI if weight or height changed
        if (updateDto.getWeightKg() != null || updateDto.getHeightCm() != null) {
            // Add BMI calculation logic here if needed
            // donorProfile.setBmi(...);
        }

        DonorProfileEntity updatedProfile = donorProfileRepository.save(donorProfile);
        log.info("Donor profile updated successfully for user ID: {}", userId);
        return modelMapper.map(updatedProfile, DonorProfileResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(value = "donorProfiles", key = "#userId")
    public DonorProfileResponseDto verifyDonor(UUID userId, DonorVerificationDto verificationDto) {
        log.info("Verifying donor profile for user ID: {}", userId);
        DonorProfileEntity donorProfile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Donor profile not found for user ID: {}", userId);
                    return new RuntimeException("Donor profile not found");
                });

        donorProfile.setVerificationStatus(verificationDto.getVerificationStatus());
//        donorProfile.setVerifiedByAdmin(verificationDto.getVerifiedByAdmin());
        
        if (verificationDto.getVerificationStatus() == VerificationStatus.VERIFIED) {
            donorProfile.setVerifiedAt(LocalDateTime.now());
            donorProfile.setEligibilityStatus(EligibilityStatus.ELIGIBLE); // Or keep as is depending on logic
        } else if (verificationDto.getVerificationStatus() == VerificationStatus.REJECTED) {
            donorProfile.setEligibilityStatus(EligibilityStatus.NOT_ELIGIBLE);
        }

        DonorProfileEntity savedProfile = donorProfileRepository.save(savedProfile);
        log.info("Donor profile verification updated for user ID: {}", userId);
        return modelMapper.map(savedProfile, DonorProfileResponseDto.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = "donorProfiles", key = "#userId")
    public void deleteDonorProfile(UUID userId) {
        log.info("Deleting donor profile for user ID: {}", userId);
        if (!donorProfileRepository.existsById(userId)) {
            log.error("Donor profile not found for user ID: {}", userId);
            throw new RuntimeException("Donor profile not found");
        }
        donorProfileRepository.deleteById(userId);
        log.info("Donor profile deleted successfully for user ID: {}", userId);
    }

    @Override
    public Page<DonorProfileResponseDto> searchDonors(DonorSearchCriteriaDto criteria, Pageable pageable) {
        log.info("Searching donors with criteria: {}", criteria);
        Page<DonorProfileEntity> donorPage = donorProfileRepository.searchDonors(
                criteria.getBloodType(),
                criteria.getEligibilityStatus(),
                criteria.getMinWeight(),
                criteria.getCity(),
                pageable
        );
        return donorPage.map(entity -> modelMapper.map(entity, DonorProfileResponseDto.class));
    }

    @Override
    @Transactional
    @CachePut(value = "donorProfiles", key = "#userId")
    public void updateDonorLocation(UUID userId, PointDTO locationDto) {
        log.info("Updating location for donor ID: {}", userId);
        DonorProfileEntity donorProfile = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Donor profile not found"));

        Point point = GeometryUtil.createPoint(locationDto);
        donorProfile.setLocation(point);
        donorProfileRepository.save(donorProfile);
        log.info("Successfully updated location for donor ID: {}", userId);
    }

    @Override
    public List<DonorProfileResponseDto> findNearbyDonors(Double latitude, Double longitude, Double radiusKm, BloodType bloodType) {
        log.info("Finding nearby donors with blood type: {}", bloodType);
        List<DonorProfileEntity> nearbyDonors = donorProfileRepository.findNearbyDonors(latitude, longitude, radiusKm);
        return nearbyDonors.stream()
                .filter(donor -> bloodType == null || donor.getBloodType().equals(bloodType))
                .map(entity -> modelMapper.map(entity, DonorProfileResponseDto.class))
                .collect(Collectors.toList());
    }
}
