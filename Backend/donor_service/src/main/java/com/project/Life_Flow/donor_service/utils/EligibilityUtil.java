package com.project.Life_Flow.donor_service.utils;

import com.project.Life_Flow.donor_service.entities.enums.Gender;
import com.project.Life_Flow.donor_service.entities.enums.VaccinationStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class EligibilityUtil {

    public static BigDecimal getMinimumHemoglobin(Gender gender){
        if (gender == Gender.FEMALE){
            return new BigDecimal("12.5");
        }else{
            return new BigDecimal("13.5");
        }
    }

    public static boolean checkVaccinationRisk(VaccinationStatus vaccinationStatus) {
        return vaccinationStatus == VaccinationStatus.RECENT_VACCINATION ||
                vaccinationStatus == VaccinationStatus.UP_TO_DATE;
    }

    public static boolean checkMedications(String medications) {
        if (medications == null || medications.trim().isEmpty()) {
            return true;
        }

        // List of medications that prevent donation
        List<String> prohibitedMeds = Arrays.asList(
                "accutane", "finasteride", "dutasteride", "isotretinoin",
                "blood thinners", "warfarin", "heparin", "aspirin"
        );

        String lowerMeds = medications.toLowerCase();
        return prohibitedMeds.stream().noneMatch(lowerMeds::contains);
    }

    public static boolean checkTravelRisk(String recentTravel) {
        if (recentTravel == null || recentTravel.trim().isEmpty()) {
            return false;
        }

        // Check for travel to malaria-endemic or high-risk areas
        List<String> riskAreas = Arrays.asList(
                "malaria", "africa", "south america", "asia", "high risk"
        );

        String lowerTravel = recentTravel.toLowerCase();
        return riskAreas.stream().anyMatch(lowerTravel::contains);
    }

    public static boolean hasSevereAllergies(String allergies) {
        if (allergies == null || allergies.trim().isEmpty()) {
            return false;
        }

        // Severe allergies that might affect donation
        List<String> severeAllergies = Arrays.asList(
                "anaphylaxis", "severe asthma", "epinephrine", "eosinophilia"
        );

        String lowerAllergies = allergies.toLowerCase();
        return severeAllergies.stream().anyMatch(lowerAllergies::contains);
    }

}
