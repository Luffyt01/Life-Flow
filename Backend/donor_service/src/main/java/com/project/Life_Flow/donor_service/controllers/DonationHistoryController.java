package com.project.Life_Flow.donor_service.controllers;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.services.DonationHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/donor")
public class DonationHistoryController {

    private final DonationHistoryService donationHistoryService;

    public DonationHistoryController(DonationHistoryService donationHistoryService) {
        this.donationHistoryService = donationHistoryService;
    }

    @GetMapping("/{donor_id}/donations")
    public ResponseEntity<List<DonationRecordDto>> getDonationHistory(
            @PathVariable("donor_id") UUID donorId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date_from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date_to) {
        return ResponseEntity.ok(donationHistoryService.getDonationHistory(donorId, limit, offset, date_from, date_to));
    }

    @GetMapping("/{donor_id}/donations/{donation_id}")
    public ResponseEntity<DonationDetailsDto> getDonationDetails(
            @PathVariable("donor_id") UUID donorId,
            @PathVariable("donation_id") UUID donationId) {
        return ResponseEntity.ok(donationHistoryService.getDonationDetails(donorId, donationId));
    }

    @PostMapping("/donations/record")
    public ResponseEntity<RecordDonationResponseDto> recordDonation(@RequestBody RecordDonationRequestDto requestDto) {
        return new ResponseEntity<>(donationHistoryService.recordDonation(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/donations/{donation_id}")
    public ResponseEntity<Void> updateDonationRecord(
            @PathVariable("donation_id") UUID donationId,
            @RequestBody UpdateDonationRecordDto requestDto) {
        donationHistoryService.updateDonationRecord(donationId, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{donor_id}/donations/stats")
    public ResponseEntity<DonationStatsDto> getDonationStats(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(donationHistoryService.getDonationStats(donorId));
    }

    @GetMapping("/{donor_id}/donations/next-eligible")
    public ResponseEntity<NextEligibleDateDto> getNextEligibleDate(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(donationHistoryService.getNextEligibleDate(donorId));
    }
}
