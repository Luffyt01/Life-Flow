package com.project.user_service.service;

import com.project.user_service.dto.hospital.HospitalProfileCreateDto;
import com.project.user_service.dto.hospital.HospitalProfileResponseDto;
import com.project.user_service.dto.hospital.HospitalProfileUpdateDto;

import java.util.UUID;

public interface HospitalProfileService {
    HospitalProfileResponseDto createHospitalProfile(UUID userId, HospitalProfileCreateDto createDto);
    HospitalProfileResponseDto getHospitalProfile(UUID userId);
    HospitalProfileResponseDto updateHospitalProfile(UUID userId, HospitalProfileUpdateDto updateDto);
    void deleteHospitalProfile(UUID userId);

    void verifyStatus(UUID hospitalId);

    boolean getVerifyStatus(UUID hospitalId);
}
