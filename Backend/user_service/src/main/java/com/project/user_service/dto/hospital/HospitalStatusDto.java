package com.project.user_service.dto.hospital;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class HospitalStatusDto {
    private boolean isVerified;

}
