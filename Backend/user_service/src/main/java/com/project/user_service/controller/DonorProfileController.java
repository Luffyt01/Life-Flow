package com.project.user_service.controller;

import com.project.user_service.dto.donor.*;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.BloodType;
import com.project.user_service.entities.enums.EligibilityStatus;
import com.project.user_service.service.DonorProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/profile/donors")
@RequiredArgsConstructor
@Slf4j
public class DonorProfileController {

    private final DonorProfileService donorProfileService;

    @PostMapping
    public ResponseEntity<DonorProfileResponseDto> createDonorProfile(HttpServletRequest request, @RequestBody DonorProfileCreateDto createDto) {

        UUID userID = getUserIdFromAuthentication();
//          log.warn("userid", authentication.getPrincipal().toString());
        return new ResponseEntity<>(donorProfileService.createDonorProfile(userID, createDto), HttpStatus.CREATED);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<DonorProfileResponseDto> getDonorProfile() {
        UUID userID = getUserIdFromAuthentication();

        return ResponseEntity.ok(donorProfileService.getDonorProfile(userID));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<DonorProfileResponseDto> getDonorProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(donorProfileService.getDonorProfile(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<DonorProfileResponseDto> updateDonorProfile( @RequestBody DonorProfileUpdateDto updateDto) {
        UUID userID = getUserIdFromAuthentication();

        return ResponseEntity.ok(donorProfileService.updateDonorProfile(userID, updateDto));
    }

    @PatchMapping("/{userId}/verify")
    public ResponseEntity<DonorProfileResponseDto> verifyDonor(@PathVariable UUID userId, @RequestBody DonorVerificationDto verificationDto) {
        return ResponseEntity.ok(donorProfileService.verifyDonor(userId, verificationDto));
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<Void> deleteDonorProfile() {
        UUID userID = getUserIdFromAuthentication();

        donorProfileService.deleteDonorProfile(userID);
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


    public UUID getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return user.getId();
    }
}
