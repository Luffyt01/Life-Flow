package com.project.user_service.dto.donor;

import com.project.user_service.dto.PointDTO;
import com.project.user_service.entities.enums.BloodType;
import lombok.Data;

import java.util.UUID;

@Data
public class DonorProfileResponseLessDto {
    private UUID donorId;
    private BloodType bloodType;
    private String address;
    private PointDTO location;
}
