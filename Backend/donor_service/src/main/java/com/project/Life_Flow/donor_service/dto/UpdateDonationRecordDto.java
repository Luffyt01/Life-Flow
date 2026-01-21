package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.enums.AdverseEvents;
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
    private List<AdverseEvents> adverseEvents;
    private RecoveryStatus recoveryStatus;
}
