package com.project.Live_Flow.request_service.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingCriteria {
    private String bloodType;
    private Double distance;
    private String availability;
}
