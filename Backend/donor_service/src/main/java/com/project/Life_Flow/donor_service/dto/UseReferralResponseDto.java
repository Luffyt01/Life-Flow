package com.project.Life_Flow.donor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UseReferralResponseDto {
    private int pointsAwarded;
    private int referrerPoints;
}
