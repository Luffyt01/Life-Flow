package com.project.Life_Flow.donor_service.services.impl;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileResponse;
import com.project.Life_Flow.donor_service.dto.EligibilityCheckResponse;
import com.project.Life_Flow.donor_service.entities.DonorGamification;
import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import com.project.Life_Flow.donor_service.exception.ResourceNotFoundException;
import com.project.Life_Flow.donor_service.repositories.DonorGamificationRepository;
import com.project.Life_Flow.donor_service.repositories.DonorProfileRepository;
import com.project.Life_Flow.donor_service.repositories.EligibilityCheckRepository;
import com.project.Life_Flow.donor_service.services.DonorService;
import com.project.Life_Flow.donor_service.utils.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonorServiceImpl implements DonorService {

    private final DonorProfileRepository donorProfileRepository;
    private final DonorGamificationRepository gamificationRepository;
    private final EligibilityCheckRepository eligibilityRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public DonorProfileDto getMyProfile(UUID userId) {

        return modelMapper.map(
                donorProfileRepository.findByUserId(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found"))
                ,DonorProfileDto.class);
    }

    @Override
    @Transactional
    public DonorProfileResponse registerDonor(DonorProfileRequestDto donorProfileRequestDto, UUID userId) {
        if (donorProfileRepository.findByUserId(userId).isPresent()){
            throw new IllegalStateException("Profile already exists for this user.");
        }

        BigDecimal heightM = BigDecimal.valueOf(donorProfileRequestDto.getHeightCm()/100);
        BigDecimal bmi = donorProfileRequestDto.getWeightKg().divide(heightM.pow(2), 2, java.math.RoundingMode.HALF_UP);
        DonorProfile donorProfile = modelMapper.map(donorProfileRequestDto, DonorProfile.class);
        donorProfile.setUserId(userId);
        donorProfile.setBmi(bmi);
        DonorProfile savedDonorProfile = donorProfileRepository.save(donorProfile);

        DonorGamification gamification = DonorGamification.builder()
                        .donor(savedDonorProfile)
                        .totalPoints(0)
                        .totalDonations(0)
                        .badgeLevel(BadgeLevel.BRONZE)
                        .estimatedLivesSaved(0)
                        .build();
        DonorGamification savedRecord = gamificationRepository.save(gamification);

        log.info("BUSINESS_EVENT: New Donor Profile created. DonorID: {}, UserID: {}",
                savedDonorProfile.getDonorId(), userId);

        return DonorProfileResponse.builder()
                .donorId(savedDonorProfile.getDonorId())
                .userId(userId)
                .bloodType(savedDonorProfile.getBloodType())
                .ageYears(0)
                .weightKg(savedDonorProfile.getWeightKg())
                .heightCm(savedDonorProfile.getHeightCm())
                .bmi(savedDonorProfile.getBmi())
                .eligibilityStatus(savedDonorProfile.getEligibilityStatus())
                .verificationStatus(savedDonorProfile.getVerificationStatus())
                .lastEligibilityCheck(savedDonorProfile.getLastEligibilityCheck())
                .badgeLevel(savedRecord.getBadgeLevel())
                .totalPoints(savedRecord.getTotalPoints())
                .build();
    }

    @Override
    public DonorProfileDto getDonorProfile(UUID donorId) {
        return null;
    }

    @Override
    public EligibilityCheckResponse checkEligibility(UUID donorId) {
        return null;
    }

    @Override
    public DonorProfile getDonorEntity(UUID donorId) {
        return null;
    }
}
