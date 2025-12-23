package com.project.Life_Flow.donor_service.controllers;

import com.project.Life_Flow.donor_service.dto.DonorProfileDto;
import com.project.Life_Flow.donor_service.dto.DonorProfileRequestDto;
import com.project.Life_Flow.donor_service.services.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donor")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;
    @PostMapping("/profile/create")
    public ResponseEntity<DonorProfileDto> createDonorProfile(@RequestBody DonorProfileRequestDto donorProfileRequestDto){
        return new ResponseEntity<>(donorService.createDonorProfile(donorProfileRequestDto), HttpStatus.CREATED);
    }
}
