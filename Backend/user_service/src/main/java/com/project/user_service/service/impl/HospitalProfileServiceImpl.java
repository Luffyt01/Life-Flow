package com.project.user_service.service.impl;

import com.project.user_service.dto.hospital.HospitalProfileCreateDto;
import com.project.user_service.dto.hospital.HospitalProfileResponseDto;
import com.project.user_service.dto.hospital.HospitalProfileUpdateDto;
import com.project.user_service.entities.HospitalProfileEntity;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.repositories.HospitalProfileRepository;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.HospitalProfileService;
import com.project.user_service.utils.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalProfileServiceImpl implements HospitalProfileService {

    private final HospitalProfileRepository hospitalProfileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public HospitalProfileResponseDto createHospitalProfile(UUID userId, HospitalProfileCreateDto createDto) {
        log.info("Creating hospital profile for user ID: {}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });

        if (hospitalProfileRepository.existsById(userId)) {
            log.warn("Hospital profile already exists for user ID: {}", userId);
            throw new RuntimeException("Hospital profile already exists");
        }

        if (hospitalProfileRepository.findByLicenseNumber(createDto.getLicenseNumber()).isPresent()) {
            log.warn("License number already registered: {}", createDto.getLicenseNumber());
            throw new RuntimeException("License number already registered");
        }

        HospitalProfileEntity hospitalProfile = modelMapper.map(createDto, HospitalProfileEntity.class);
        hospitalProfile.setUser(user);
        hospitalProfile.setVerified(false);
        
        // Handle Geometry Point creation
        if (createDto.getLatitude() != null && createDto.getLongitude() != null) {
            Point location = GeometryUtil.createPoint(createDto.getLatitude(), createDto.getLongitude());
            hospitalProfile.setLocation(location);
        }

        HospitalProfileEntity savedProfile = hospitalProfileRepository.save(hospitalProfile);
        log.info("Hospital profile created successfully for user ID: {}", userId);
        return mapToResponseDto(savedProfile);
    }

    @Override
    public HospitalProfileResponseDto getHospitalProfile(UUID userId) {
        log.info("Fetching hospital profile for user ID: {}", userId);
        HospitalProfileEntity hospitalProfile = hospitalProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Hospital profile not found for user ID: {}", userId);
                    return new RuntimeException("Hospital profile not found");
                });
        return mapToResponseDto(hospitalProfile);
    }

    @Override
    @Transactional
    public HospitalProfileResponseDto updateHospitalProfile(UUID userId, HospitalProfileUpdateDto updateDto) {
        log.info("Updating hospital profile for user ID: {}", userId);
        HospitalProfileEntity hospitalProfile = hospitalProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Hospital profile not found for user ID: {}", userId);
                    return new RuntimeException("Hospital profile not found");
                });

        // Map non-null values
        modelMapper.map(updateDto, hospitalProfile);

        // Handle Geometry Point update
        if (updateDto.getLatitude() != null && updateDto.getLongitude() != null) {
            Point location = GeometryUtil.createPoint(updateDto.getLatitude(), updateDto.getLongitude());
            hospitalProfile.setLocation(location);
        }

        HospitalProfileEntity updatedProfile = hospitalProfileRepository.save(hospitalProfile);
        log.info("Hospital profile updated successfully for user ID: {}", userId);
        return mapToResponseDto(updatedProfile);
    }

    @Override
    @Transactional
    public void deleteHospitalProfile(UUID userId) {
        log.info("Deleting hospital profile for user ID: {}", userId);
        if (!hospitalProfileRepository.existsById(userId)) {
            log.error("Hospital profile not found for user ID: {}", userId);
            throw new RuntimeException("Hospital profile not found");
        }
        hospitalProfileRepository.deleteById(userId);
        log.info("Hospital profile deleted successfully for user ID: {}", userId);
    }

    private HospitalProfileResponseDto mapToResponseDto(HospitalProfileEntity entity) {
        HospitalProfileResponseDto dto = modelMapper.map(entity, HospitalProfileResponseDto.class);
        if (entity.getLocation() != null) {
            dto.setLatitude(entity.getLocation().getY());
            dto.setLongitude(entity.getLocation().getX());
        }
        return dto;
    }
}
