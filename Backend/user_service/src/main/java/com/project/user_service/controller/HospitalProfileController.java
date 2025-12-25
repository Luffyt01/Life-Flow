package com.project.user_service.controller;

import com.project.user_service.dto.hospital.HospitalProfileCreateDto;
import com.project.user_service.dto.hospital.HospitalProfileResponseDto;
import com.project.user_service.dto.hospital.HospitalProfileUpdateDto;
import com.project.user_service.dto.hospital.HospitalStatusDto;
import com.project.user_service.service.HospitalProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalProfileController {

    private final HospitalProfileService hospitalProfileService;

    @PostMapping("/register/{userId}")
    public ResponseEntity<HospitalProfileResponseDto> createHospitalProfile(@PathVariable UUID userId, @RequestBody HospitalProfileCreateDto createDto) {
        return new ResponseEntity<>(hospitalProfileService.createHospitalProfile(userId, createDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<HospitalProfileResponseDto> getHospitalProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(hospitalProfileService.getHospitalProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HospitalProfileResponseDto> updateHospitalProfile(@PathVariable UUID userId, @RequestBody HospitalProfileUpdateDto updateDto) {
        return ResponseEntity.ok(hospitalProfileService.updateHospitalProfile(userId, updateDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteHospitalProfile(@PathVariable UUID userId) {
        hospitalProfileService.deleteHospitalProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/verification-status/{hospitalId}")
    public ResponseEntity<String> verifyStatusChange(@PathVariable UUID hospitalId){
    hospitalProfileService.verifyStatus(hospitalId);
    return ResponseEntity.ok("Status changed successfully");
    }
    @GetMapping("/verification-status/{hospitalId}")
    public ResponseEntity<HospitalStatusDto> getVerifyStatusChange(@PathVariable UUID hospitalId){
    boolean isVerified = hospitalProfileService.getVerifyStatus(hospitalId);
    return new ResponseEntity<>(HospitalStatusDto.builder().isVerified(isVerified).build(),HttpStatus.OK);
    }
}
