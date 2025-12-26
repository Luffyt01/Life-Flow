package com.project.Life_Flow.donor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementDto {
    private UUID achievementId;
    private String name;
    private String description;
    private LocalDate earnedDate;
    private int points;
}
