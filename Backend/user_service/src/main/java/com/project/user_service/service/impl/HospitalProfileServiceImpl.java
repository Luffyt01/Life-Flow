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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalProfileServiceImpl implements HospitalProfileService {

    private final HospitalProfileRepository hospitalProfileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    @CachePut(value = "hospitalProfiles", key = "#userId")
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


        HospitalProfileEntity savedProfile = hospitalProfileRepository.save(hospitalProfile);
        log.info("Hospital profile created successfully for user ID: {}", userId);
        return modelMapper.map(savedProfile, HospitalProfileResponseDto.class);
    }

    @Override
    @Cacheable(value = "hospitalProfiles", key = "#userId")
    public HospitalProfileResponseDto getHospitalProfile(UUID userId) {
        log.info("Fetching hospital profile for user ID: {}", userId);
        HospitalProfileEntity hospitalProfile = hospitalProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Hospital profile not found for user ID: {}", userId);
                    return new RuntimeException("Hospital profile not found");
                });
        return modelMapper.map(hospitalProfile, HospitalProfileResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(value = "hospitalProfiles", key = "#userId")
    public HospitalProfileResponseDto updateHospitalProfile(UUID userId, HospitalProfileUpdateDto updateDto) {
        log.info("Updating hospital profile for user ID: {}", userId);
        HospitalProfileEntity hospitalProfile = hospitalProfileRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Hospital profile not found for user ID: {}", userId);
                    return new RuntimeException("Hospital profile not found");
                });

        // Map non-null values
        modelMapper.map(updateDto, hospitalProfile);

        HospitalProfileEntity updatedProfile = hospitalProfileRepository.save(hospitalProfile);
        log.info("Hospital profile updated successfully for user ID: {}", userId);
        return modelMapper.map(updatedProfile, HospitalProfileResponseDto.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = "hospitalProfiles", key = "#userId")
    public void deleteHospitalProfile(UUID userId) {
        log.info("Deleting hospital profile for user ID: {}", userId);
        if (!hospitalProfileRepository.existsById(userId)) {
            log.error("Hospital profile not found for user ID: {}", userId);
            throw new RuntimeException("Hospital profile not found");
        }
        hospitalProfileRepository.deleteById(userId);
        log.info("Hospital profile deleted successfully for user ID: {}", userId);
    }

    @Override
    public boolean getVerifyStatus(UUID hospitalId) {
        return hospitalProfileRepository.isVerified(hospitalId);
    }

    @Override
    @CacheEvict(value = "hospitalProfiles", key = "#hospitalId")
    public void verifyStatus(UUID hospitalId) {
        try {
            hospitalProfileRepository.updateVerifyStatusById(hospitalId);
        }catch (Exception e){
            log.error("error in verifying status",e);
            throw new RuntimeException("error in verifying status");
        }

    }

    @Override
    public List<HospitalProfileResponseDto> searchHospitals(String city) {
        log.info("Searching hospitals in city: {}", city);
        List<HospitalProfileEntity> hospitals = hospitalProfileRepository.findByCity(city);
        return hospitals.stream()
                .map(entity -> modelMapper.map(entity, HospitalProfileResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalProfileResponseDto> findNearbyHospitals(Double latitude, Double longitude, Double radiusKm) {
        log.info("Finding nearby hospitals at lat: {}, lon: {}, radius: {}km", latitude, longitude, radiusKm);
        List<HospitalProfileEntity> nearbyHospitals = hospitalProfileRepository.findNearbyHospitals(latitude, longitude, radiusKm);
        return nearbyHospitals.stream()
                .map(entity -> modelMapper.map(entity, HospitalProfileResponseDto.class))
                .collect(Collectors.toList());
    }
}
