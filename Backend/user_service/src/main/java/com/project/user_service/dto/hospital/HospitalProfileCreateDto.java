package com.project.user_service.dto.hospital;

import com.project.user_service.dto.PointDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalProfileCreateDto {
    private String name;
    private String licenseNumber;
    private String documentDriveUrl;
    private String address;
    private PointDTO location;
    private String contactPerson;
    private String emergencyContact;
    private String description;
    private String website;
}
