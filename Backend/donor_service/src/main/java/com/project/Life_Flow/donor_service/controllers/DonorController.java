package com.project.Life_Flow.donor_service.controllers;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileResponse;
import com.project.Life_Flow.donor_service.dto.EligibilityCheckResponse;
import com.project.Life_Flow.donor_service.services.DonorService;
import com.project.Life_Flow.donor_service.utils.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/donor")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;
    private final JwtParser jwtParser;

    @GetMapping("/profile/me")
    public ResponseEntity<DonorProfileDto> getMyProfile(HttpServletRequest request){
        return ResponseEntity.ok(donorService.getMyProfile(jwtParser.getUserId(request)));
    }

    @PostMapping("/profile/create")
    public ResponseEntity<DonorProfileResponse> createDonorProfile(@RequestBody DonorProfileRequestDto donorProfileRequestDto,
                                                                   HttpServletRequest request){
        return new ResponseEntity<>(donorService.registerDonor(
                donorProfileRequestDto,
                jwtParser.getUserId(request)),
                HttpStatus.CREATED);
    }

    @GetMapping("/{donorId}")
    public ResponseEntity<DonorProfileDto> getDonorById(@PathVariable UUID donorId) {
        return ResponseEntity.ok(donorService.getDonorProfile(donorId));
    }

    @GetMapping("/me/eligibility")
    public ResponseEntity<EligibilityCheckResponse> checkMyEligibility(HttpServletRequest request) {
        // First get profile ID from User ID
        DonorProfileDto profile = donorService.getMyProfile(jwtParser.getUserId(request));
        return ResponseEntity.ok(donorService.checkEligibility(profile.getDonorId()));
    }

    @GetMapping("/{id}/eligibility")
    public ResponseEntity<EligibilityCheckResponse> checkDonorEligibility(@PathVariable UUID id) {
        return ResponseEntity.ok(donorService.checkEligibility(id));
    }
}
