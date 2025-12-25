package com.project.user_service.dto.hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalProfileDto {
    private UUID hospitalId;
    private String name;
    private String licenseNumber;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String contactPerson;
    private String emergencyContact;
    private boolean isVerified;
    private LocalDateTime verificationDate;
    private LocalDateTime createdAt;
}
