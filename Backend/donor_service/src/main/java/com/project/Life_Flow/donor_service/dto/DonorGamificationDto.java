package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorGamificationDto {
    private String gamificationId;
    private UUID donor;
    private Integer totalPoints;
    private Integer totalDonations;
    private Integer estimatedLivesSaved;
    private BadgeLevel badgeLevel;
    private String achievementMilestones;
    private Integer heroOfMonthCount;
    private Integer heroOfYearCount;
    private Integer topDonorRank;
    private Integer loyaltyScore;
    private Integer referralCount;
    private LocalDate lastDonationDate;
    private Integer consecutiveDonations;
    private LocalDateTime lastUpdated;
}
