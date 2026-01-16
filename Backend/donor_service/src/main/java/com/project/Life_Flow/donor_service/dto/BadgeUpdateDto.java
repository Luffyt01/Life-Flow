package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeUpdateDto {
    private BadgeLevel oldBadge;
    private BadgeLevel newBadge;
    private LocalDateTime achievedAt;
}
