package com.project.Life_Flow.donor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntryDto {
    private int rank;
    private UUID donorId;
    private String name;
    private int points;
    private int donations;
    private int emergencyResponses;
}
