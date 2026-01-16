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
public class UseReferralRequestDto {
    private String referralCode;
    private UUID referredDonorId;
}
