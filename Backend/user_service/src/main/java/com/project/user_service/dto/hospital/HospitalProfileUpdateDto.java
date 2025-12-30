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
public class HospitalProfileUpdateDto {
    private String contactPerson;
    private String emergencyContact;
//    private String address;
    private String description;
    private String website;
//    private String documentDriveUrl;
    private PointDTO location;
}
