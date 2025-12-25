package com.project.user_service.dto.donor;

import com.project.user_service.entities.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorVerificationDto {
    private VerificationStatus verificationStatus;
//    private String verifiedByAdmin; // Admin ID or Name
}
