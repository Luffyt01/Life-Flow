package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DonationHistoryService {
    List<DonationRecordDto> getDonationHistory(UUID donorId, int limit, int offset, LocalDate dateFrom, LocalDate dateTo);
    DonationDetailsDto getDonationDetails(UUID donorId, UUID donationId);
    RecordDonationResponseDto recordDonation(RecordDonationRequestDto requestDto);
    void updateDonationRecord(UUID donationId, UpdateDonationRecordDto requestDto);
    DonationStatsDto getDonationStats(UUID donorId);
    NextEligibleDateDto getNextEligibleDate(UUID donorId);
}
