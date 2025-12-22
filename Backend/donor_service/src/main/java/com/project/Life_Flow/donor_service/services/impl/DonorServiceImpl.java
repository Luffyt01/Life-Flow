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
import java.time.LocalDate;
import java.time.Period;
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
    public DonorProfileResponse getMyProfile(UUID userId) {
        DonorProfile donor = donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor profile not found for user"));
        return getDonorProfile(donor.getDonorId());
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

        // Initialize gamification profile
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

        return mapToProfileResponse(savedDonorProfile, savedRecord);
    }

    @Override
    public DonorProfileResponse getDonorProfile(UUID donorId) {
        DonorProfile donor = getDonorEntity(donorId);
        DonorGamification gamification = gamificationRepository.findByDonor(donor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gamification profile not found for donor with id: "+donor.getDonorId()
                ));
        return mapToProfileResponse(donor, gamification);
    }

    @Override
    public EligibilityCheckResponse checkEligibility(UUID donorId) {
//        DonorProfile donor = getDonorEntity(donorId);
//        boolean isEligible = true;
//        String reason = "";
//
//        // Eligibility Check conditions
//        // 1. Age check (18-65 standard)
//        int age = Period.between(donor.getDateOfBirth(), )
        return null;
    }

    @Override
    public DonorProfile getDonorEntity(UUID donorId) {
        return donorProfileRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: "+donorId));
    }

    private DonorProfileResponse mapToProfileResponse(DonorProfile donor, DonorGamification game) {
        return DonorProfileResponse.builder()
                .donorId(donor.getDonorId())
                .userId(donor.getUserId())
                .bloodType(donor.getBloodType())
                .ageYears(Period.between(donor.getDateOfBirth(), LocalDate.now()).getYears())
                .weightKg(donor.getWeightKg())
                .bmi(donor.getBmi())
                .eligibilityStatus(donor.getEligibilityStatus())
                .verificationStatus(donor.getVerificationStatus())
                .badgeLevel(game.getBadgeLevel())
                .totalPoints(game.getTotalPoints())
                .build();
    }
}
