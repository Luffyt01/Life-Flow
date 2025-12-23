package com.project.Life_Flow.donor_service.services.impl;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileResponse;
import com.project.Life_Flow.donor_service.dto.EligibilityCheckResponse;
import com.project.Life_Flow.donor_service.entities.DonorGamification;
import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.EligibilityCheck;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.project.Life_Flow.donor_service.utils.EligibilityUtil.*;

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
        DonorProfile donor = getDonorEntity(donorId);
        DonorGamification gamification = gamificationRepository.findByDonor(donor)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found for donor with id: "+donor.getDonorId()));

        LocalDate lastDonationDate = gamification.getLastDonationDate();
        boolean isEligible = true;
        List<String> rejectionReasons = new ArrayList<>();

        // Eligibility Check conditions
        // 1. Age check (18-65 standard)
        int age = Period.between(donor.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18 || age > 65) {
            isEligible = false;
            rejectionReasons.add("Age must be between 18 and 65 years");
        }

        // 2. Weight Check (Min 50kg)
        BigDecimal minWeight = new BigDecimal("50");
        if (donor.getWeightKg().compareTo(minWeight) < 0) {
            isEligible = false;
            rejectionReasons.add("Weight is below minimum requirement (50kg)");
        }

        // 3. Hemoglobin Level Check
        BigDecimal minHemoglobin = getMinimumHemoglobin(donor.getGender());
        if (donor.getHemoglobinLevel().compareTo(minHemoglobin) < 0) {
            isEligible = false;
            rejectionReasons.add("Hemoglobin level is below minimum requirement");
        }

        // 4. Check time since last donation (at least 56 days for whole blood)
        if (lastDonationDate != null) {
            long daysSinceLastDonation = ChronoUnit.DAYS.between(
                    lastDonationDate, LocalDate.now()
            );
            if (daysSinceLastDonation < 56) {
                isEligible = false;
                rejectionReasons.add("Must wait at least 56 days between donations");
            }
        }

        // 5. Check for chronic diseases
        if (donor.getChronicDiseases() != null &&
                !donor.getChronicDiseases().trim().isEmpty()) {
            isEligible = false;
            rejectionReasons.add("Chronic diseases present");
        }

        // 6. Check recent tattoos/piercings (must be at least 3 months ago)
        if (donor.getTattooDate() != null) {
            Period timeSinceTattoo = Period.between(donor.getTattooDate(), LocalDate.now());
            if (timeSinceTattoo.getMonths() < 3) {
                isEligible = false;
                rejectionReasons.add("Recent tattoo/piercing within last 3 months");
            }
        }

        // 7. Check recent vaccinations
        boolean hasVaccinationRisk = checkVaccinationRisk(donor.getVaccinationStatus());
        if (hasVaccinationRisk) {
            isEligible = false;
            rejectionReasons.add("Recent vaccination requiring deferral");
        }

        // 8. Check medications
        boolean medicationsAllowed = checkMedications(donor.getMedications());
        if (!medicationsAllowed) {
            isEligible = false;
            rejectionReasons.add("Medications not allowed for donation");
        }

        // 9. Check travel history
        boolean hasTravelRisk = checkTravelRisk(donor.getRecentTravel());
        if (hasTravelRisk) {
            isEligible = false;
            rejectionReasons.add("Recent travel to high-risk area");
        }

        // 10. Check allergies that might affect donation
        if (donor.getAllergies() != null &&
                hasSevereAllergies(donor.getAllergies())) {
            isEligible = false;
            rejectionReasons.add("Severe allergies present");
        }


        // Combine all rejection reasons
        String reason = isEligible ? "" : String.join("; ", rejectionReasons);

        // Create eligibility check record
        EligibilityCheck check = new EligibilityCheck();
        check.setDonor(donor);
        check.setOverallEligible(isEligible);
        check.setReasonIfIneligible(reason);
        check.setAgeYears(age);
        check.setMinWeightKg(minWeight);
        check.setMinHemoglobin(minHemoglobin);
        check.setMedicalConditionsClear(donor.getChronicDiseases() == null ||
                donor.getChronicDiseases().trim().isEmpty());
        check.setMedicationsAllowed(medicationsAllowed);
        check.setTravelRisk(hasTravelRisk);
        check.setTattooRisk(donor.getTattooDate() != null &&
                Period.between(donor.getTattooDate(), LocalDate.now()).getMonths() < 3);
        check.setVaccinationRisk(hasVaccinationRisk);

        // Calculate days since last donation
        if (lastDonationDate != null) {
            check.setLastDonationDays((int) ChronoUnit.DAYS.between(
                    lastDonationDate, LocalDate.now()
            ));
        }

        // Save the eligibility check
        EligibilityCheck savedCheck = eligibilityRepository.save(check);

        // Create and return response (assuming EligibilityCheckResponse exists)
        return createEligibilityResponse(savedCheck);
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

    private EligibilityCheckResponse createEligibilityResponse(EligibilityCheck check) {
        return EligibilityCheckResponse.builder()
                .checkId(check.getCheckId())
                .checkTimestamp(check.getCheckTimestamp())
                .overallEligible(check.getOverallEligible())
                .reasonIfIneligible(check.getReasonIfIneligible())
                .lastDonationDays(check.getLastDonationDays())
                .medicalConditionsClear(check.getMedicalConditionsClear())
                .travelRisk(check.getTravelRisk())
                .vaccinationRisk(check.getVaccinationRisk())
                .build();
    }
}
