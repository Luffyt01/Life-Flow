package com.project.Life_Flow.donor_service.controllers;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.services.DonorAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/donor/{donor_id}/availability")
public class DonorAvailabilityController {

    private final DonorAvailabilityService donorAvailabilityService;

    public DonorAvailabilityController(DonorAvailabilityService donorAvailabilityService) {
        this.donorAvailabilityService = donorAvailabilityService;
    }

    @PutMapping
    public ResponseEntity<UpdateAvailabilityResponseDto> updateDonorAvailability(@PathVariable("donor_id") UUID donorId, @Valid @RequestBody DonorAvailabilityDto availabilityDto) {
        return ResponseEntity.ok(donorAvailabilityService.updateDonorAvailability(donorId, availabilityDto));
    }

    @GetMapping
    public ResponseEntity<List<DonorAvailabilityDto>> getDonorAvailability(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(donorAvailabilityService.getDonorAvailability(donorId));
    }

    @GetMapping("/current")
    public ResponseEntity<CurrentAvailabilityDto> getCurrentDonorAvailability(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(donorAvailabilityService.getCurrentDonorAvailability(donorId));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkAvailabilityUpdateResponseDto> bulkUpdateDonorAvailability(@PathVariable("donor_id") UUID donorId, @Valid @RequestBody BulkAvailabilityUpdateRequestDto requestDto) {
        return ResponseEntity.ok(donorAvailabilityService.bulkUpdateDonorAvailability(donorId, requestDto));
    }

    @PutMapping("/emergency")
    public ResponseEntity<Void> updateEmergencyAvailability(@PathVariable("donor_id") UUID donorId, @Valid @RequestBody EmergencyAvailabilityDto emergencyAvailabilityDto) {
        donorAvailabilityService.updateEmergencyAvailability(donorId, emergencyAvailabilityDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{day}")
    public ResponseEntity<Void> deleteDonorAvailability(@PathVariable("donor_id") UUID donorId, @PathVariable("day") String day) {
        donorAvailabilityService.deleteDonorAvailability(donorId, day);
        return ResponseEntity.noContent().build();
    }
}
