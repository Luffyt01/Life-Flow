package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.entities.DonationHistory;
import com.project.Life_Flow.donor_service.entities.enums.DonationStatus;
import com.project.Life_Flow.donor_service.exception.ResourceNotFoundException;
import com.project.Life_Flow.donor_service.repositories.DonationHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonationHistoryServiceImpl implements DonationHistoryService {

    private final DonationHistoryRepository donationHistoryRepository;

    public DonationHistoryServiceImpl(DonationHistoryRepository donationHistoryRepository) {
        this.donationHistoryRepository = donationHistoryRepository;
    }

    @Override
    public List<DonationRecordDto> getDonationHistory(UUID donorId, int limit, int offset, LocalDate dateFrom, LocalDate dateTo) {
        // This is a simplified implementation. A real implementation would use a more complex query
        // to filter by date range.
        return donationHistoryRepository.findAllByDonorId(donorId, PageRequest.of(offset / limit, limit)).stream()
                .map(this::mapToRecordDto)
                .collect(Collectors.toList());
    }

    @Override
    public DonationDetailsDto getDonationDetails(UUID donorId, UUID donationId) {
        DonationHistory donationHistory = donationHistoryRepository.findByDonationIdAndDonorId(donationId, donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation record not found with id: " + donationId + " for donor: " + donorId));

        return DonationDetailsDto.builder()
                .donationDetails(donationHistory)
                .healthMetrics(null) // Placeholder
                .staffNotes(donationHistory.getStaffNotes())
                .complications(donationHistory.getComplications())
                .build();
    }

    @Override
    public RecordDonationResponseDto recordDonation(RecordDonationRequestDto requestDto) {
        DonationHistory donationHistory = new DonationHistory();
        donationHistory.setDonorId(requestDto.getDonorId());
        donationHistory.setAppointmentSlotId(requestDto.getAppointmentSlotId());
        donationHistory.setBloodTypeCollected(requestDto.getBloodType());
        donationHistory.setUnitsCollected(requestDto.getUnits());
        donationHistory.setStaffNotes(requestDto.getStaffNotes());
        donationHistory.setDonationDate(new Timestamp(System.currentTimeMillis()));
        // Set other fields from requestDto...

        DonationHistory savedDonation = donationHistoryRepository.save(donationHistory);

        // Award points (simplified)
        int pointsAwarded = 100;

        return RecordDonationResponseDto.builder()
                .donationId(savedDonation.getDonationId())
                .pointsAwarded(pointsAwarded)
                .build();
    }

    @Override
    public void updateDonationRecord(UUID donationId, UpdateDonationRecordDto requestDto) {
        DonationHistory donationHistory = donationHistoryRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation record not found with id: " + donationId));

        donationHistory.setHealthScore(requestDto.getHealthScore());
        donationHistory.setStaffNotes(requestDto.getStaffNotes());
        donationHistory.setComplications(requestDto.getComplications());
        donationHistory.setRecoveryStatus(requestDto.getRecoveryStatus());

        donationHistoryRepository.save(donationHistory);
    }

    @Override
    public DonationStatsDto getDonationStats(UUID donorId) {
        List<DonationHistory> donations = donationHistoryRepository.findAllByDonorId(donorId);
        // This is a simplified implementation.
        long totalDonations = donations.size();
        return DonationStatsDto.builder()
                .totalDonations(totalDonations)
                .totalUnits(null) // Placeholder
                .avgFrequencyDays(0) // Placeholder
                .lastDonationDate(null) // Placeholder
                .healthTrends(null) // Placeholder
                .build();
    }

    @Override
    public NextEligibleDateDto getNextEligibleDate(UUID donorId) {
        // Simplified logic: assumes a 56-day waiting period between donations.
        List<DonationHistory> donations = donationHistoryRepository.findAllByDonorId(donorId);
        if (donations.isEmpty()) {
            return NextEligibleDateDto.builder()
                    .nextEligibleDate(LocalDate.now())
                    .daysRemaining(0)
                    .reason("No previous donations.")
                    .build();
        }

        donations.sort((d1, d2) -> d2.getDonationDate().compareTo(d1.getDonationDate()));
        LocalDate lastDonationDate = donations.get(0).getDonationDate().toLocalDateTime().toLocalDate();
        LocalDate nextEligibleDate = lastDonationDate.plusDays(56);
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), nextEligibleDate);

        return NextEligibleDateDto.builder()
                .nextEligibleDate(nextEligibleDate)
                .daysRemaining(daysRemaining > 0 ? daysRemaining : 0)
                .reason("Standard 56-day waiting period.")
                .build();
    }

    private DonationRecordDto mapToRecordDto(DonationHistory donationHistory) {
        return DonationRecordDto.builder()
                .donationId(donationHistory.getDonationId())
                .date(donationHistory.getDonationDate().toLocalDateTime())
                .bloodType(donationHistory.getBloodTypeCollected())
                .units(donationHistory.getUnitsCollected())
                .healthScore(donationHistory.getHealthScore())
                .centerName("Unknown") // Placeholder
                .status(DonationStatus.SUCCESSFUL) // Placeholder
                .build();
    }
}
