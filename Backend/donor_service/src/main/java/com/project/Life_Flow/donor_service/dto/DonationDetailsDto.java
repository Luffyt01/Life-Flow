package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonationHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationDetailsDto {
    private DonationHistory donationDetails;
    private Object healthMetrics; // Placeholder for a more specific DTO
    private String staffNotes;
    private List<DonationHistory.Complication> complications;
}
