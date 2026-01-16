package com.project.user_service.dto.hospital;

import com.project.user_service.dto.PointDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalProfileResponseDto {
    private UUID hospitalId;
    private String name;
    private String licenseNumber;
    private String documentDriveUrl;
    private String address;
    private PointDTO location;
    private String contactPerson;
    private String emergencyContact;
    private boolean isVerified;
    private LocalDateTime verificationDate;
    private String description;
    private String website;
    private LocalDateTime createdAt;
}
