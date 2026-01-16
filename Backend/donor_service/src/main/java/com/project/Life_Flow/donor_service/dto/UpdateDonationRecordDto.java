package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonationHistory;
import com.project.Life_Flow.donor_service.entities.enums.RecoveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDonationRecordDto {
    private Integer healthScore;
    private String staffNotes;
    private List<DonationHistory.Complication> complications;
    private RecoveryStatus recoveryStatus;
}
