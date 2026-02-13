package com.project.user_service.controller;

import com.project.user_service.dto.hospital.HospitalProfileCreateDto;
import com.project.user_service.dto.hospital.HospitalProfileResponseDto;
import com.project.user_service.dto.hospital.HospitalProfileUpdateDto;
import com.project.user_service.dto.hospital.HospitalStatusDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.service.HospitalProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile/hospitals")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HOSPITAL')")
public class HospitalProfileController {

    private final HospitalProfileService hospitalProfileService;

    @PostMapping("/register")
    public ResponseEntity<HospitalProfileResponseDto> createHospitalProfile( @RequestBody HospitalProfileCreateDto createDto) {
      UUID userId = getUserIdFromAuthentication();
        return new ResponseEntity<>(hospitalProfileService.createHospitalProfile(userId, createDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<HospitalProfileResponseDto> getHospitalProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(hospitalProfileService.getHospitalProfile(userId));
    }
    @GetMapping("/get-profile")
    public ResponseEntity<HospitalProfileResponseDto> getHospitalProfile() {
        UUID userId = getUserIdFromAuthentication();
        return ResponseEntity.ok(hospitalProfileService.getHospitalProfile(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<HospitalProfileResponseDto> updateHospitalProfile( @RequestBody HospitalProfileUpdateDto updateDto) {
        UUID userId = getUserIdFromAuthentication();
        return ResponseEntity.ok(hospitalProfileService.updateHospitalProfile(userId, updateDto));
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<Void> deleteHospitalProfile() {
        UUID userId = getUserIdFromAuthentication();
        hospitalProfileService.deleteHospitalProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-verification-status/{hospitalId}")
    public ResponseEntity<String> verifyStatusChange(@PathVariable UUID hospitalId){
    hospitalProfileService.verifyStatus(hospitalId);
    return ResponseEntity.ok("Status changed successfully");
    }
    @PutMapping("/update-verification-status")
    public ResponseEntity<String> verifyStatusChange(){
        UUID hospitalId = getUserIdFromAuthentication();
    hospitalProfileService.verifyStatus(hospitalId);
    return ResponseEntity.ok("Status changed successfully");
    }
    @GetMapping("/verification-status")
    public ResponseEntity<HospitalStatusDto> getVerifyStatusChange(){
        UUID hospitalId = getUserIdFromAuthentication();
    boolean isVerified = hospitalProfileService.getVerifyStatus(hospitalId);
    return new ResponseEntity<>(HospitalStatusDto.builder().isVerified(isVerified).build(),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HospitalProfileResponseDto>> searchHospitals(@RequestParam(required = false) String city) {
        return ResponseEntity.ok(hospitalProfileService.searchHospitals(city));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<HospitalProfileResponseDto>> findNearbyHospitals(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusKm
    ) {
        return ResponseEntity.ok(hospitalProfileService.findNearbyHospitals(latitude, longitude, radiusKm));
    }

    public UUID getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return user.getId();
    }
}
