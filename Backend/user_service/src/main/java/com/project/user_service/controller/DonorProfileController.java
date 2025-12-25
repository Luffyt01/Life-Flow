package com.project.user_service.controller;

import com.project.user_service.dto.donor.*;
import com.project.user_service.entities.enums.BloodType;
import com.project.user_service.entities.enums.EligibilityStatus;
import com.project.user_service.service.DonorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorProfileController {

    private final DonorProfileService donorProfileService;

    @PostMapping("/{userId}")
    public ResponseEntity<DonorProfileResponseDto> createDonorProfile(@PathVariable UUID userId, @RequestBody DonorProfileCreateDto createDto) {
        return new ResponseEntity<>(donorProfileService.createDonorProfile(userId, createDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DonorProfileResponseDto> getDonorProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(donorProfileService.getDonorProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<DonorProfileResponseDto> updateDonorProfile(@PathVariable UUID userId, @RequestBody DonorProfileUpdateDto updateDto) {
        return ResponseEntity.ok(donorProfileService.updateDonorProfile(userId, updateDto));
    }

    @PatchMapping("/{userId}/verify")
    public ResponseEntity<DonorProfileResponseDto> verifyDonor(@PathVariable UUID userId, @RequestBody DonorVerificationDto verificationDto) {
        return ResponseEntity.ok(donorProfileService.verifyDonor(userId, verificationDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteDonorProfile(@PathVariable UUID userId) {
        donorProfileService.deleteDonorProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DonorProfileResponseDto>> searchDonors(
            @RequestParam(required = false) BloodType bloodType,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal minWeight,
            @RequestParam(required = false) EligibilityStatus eligibilityStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        DonorSearchCriteriaDto criteria = DonorSearchCriteriaDto.builder()
                .bloodType(bloodType)
                .city(city)
                .minWeight(minWeight)
                .eligibilityStatus(eligibilityStatus)
                .build();

        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.ok(donorProfileService.searchDonors(criteria, pageable));
    }
}
