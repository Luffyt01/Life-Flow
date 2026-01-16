package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamificationProfileDto {
    private int points;
    private BadgeLevel badgeLevel;
    private int donationsCount;
    private int emergencyResponses;
    private int livesSaved;
    private int streakDays;
    private List<AchievementDto> achievements;
}
