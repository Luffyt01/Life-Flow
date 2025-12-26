package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.dto.enums.DayOfWeek;
import com.project.Life_Flow.donor_service.entities.DonorAvailability;
import com.project.Life_Flow.donor_service.exception.ResourceNotFoundException;
import com.project.Life_Flow.donor_service.repositories.DonorAvailabilityRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonorAvailabilityServiceImpl implements DonorAvailabilityService {

    private final DonorAvailabilityRepository donorAvailabilityRepository;

    public DonorAvailabilityServiceImpl(DonorAvailabilityRepository donorAvailabilityRepository) {
        this.donorAvailabilityRepository = donorAvailabilityRepository;
    }

    @Override
    public UpdateAvailabilityResponseDto updateDonorAvailability(UUID donorId, DonorAvailabilityDto availabilityDto) {
        DonorAvailability donorAvailability = donorAvailabilityRepository.findByDonorId(donorId)
                .orElse(new DonorAvailability());

        donorAvailability.setDonorId(donorId);
        donorAvailability.setDayOfWeek(availabilityDto.getDayOfWeek().ordinal());
        donorAvailability.setTimeSlots(availabilityDto.getTimeSlots().stream()
                .map(timeSlotDto -> new DonorAvailability.TimeSlot(timeSlotDto.getStartTime().toString(), timeSlotDto.getEndTime().toString()))
                .collect(Collectors.toList()));
        donorAvailability.setPreferredRadiusKm(availabilityDto.getPreferredRadiusKm());
        donorAvailability.setIsAvailableEmergency(availabilityDto.getIsAvailableEmergency());
        donorAvailability.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        DonorAvailability savedAvailability = donorAvailabilityRepository.save(donorAvailability);

        return UpdateAvailabilityResponseDto.builder()
                .availabilityId(savedAvailability.getAvailabilityId())
                .updatedAt(savedAvailability.getLastUpdated().toLocalDateTime())
                .build();
    }

    @Override
    public List<DonorAvailabilityDto> getDonorAvailability(UUID donorId) {
        return donorAvailabilityRepository.findAllByDonorId(donorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CurrentAvailabilityDto getCurrentDonorAvailability(UUID donorId) {
        // This is a simplified implementation. A real implementation would involve more complex logic
        // to check current time against the available time slots for the current day.
        List<DonorAvailability> availabilities = donorAvailabilityRepository.findAllByDonorId(donorId);
        LocalDateTime now = LocalDateTime.now();
        java.time.DayOfWeek currentDay = now.getDayOfWeek();

        boolean isAvailableNow = availabilities.stream()
                .filter(a -> a.getDayOfWeek() == currentDay.getValue())
                .anyMatch(a -> a.getTimeSlots().stream()
                        .anyMatch(ts -> {
                            // Simplified logic, does not handle time zones well
                            return now.toLocalTime().isAfter(java.time.LocalTime.parse(ts.getStartTime())) &&
                                   now.toLocalTime().isBefore(java.time.LocalTime.parse(ts.getEndTime()));
                        }));

        return CurrentAvailabilityDto.builder()
                .isAvailableNow(isAvailableNow)
                .nextAvailableTime(null) // Placeholder
                .locationStatus("UNKNOWN") // Placeholder
                .build();
    }

    @Override
    public BulkAvailabilityUpdateResponseDto bulkUpdateDonorAvailability(UUID donorId, BulkAvailabilityUpdateRequestDto requestDto) {
        List<DonorAvailability> availabilities = requestDto.getAvailability().stream()
                .map(dto -> {
                    DonorAvailability availability = new DonorAvailability();
                    availability.setDonorId(donorId);
                    availability.setDayOfWeek(dto.getDayOfWeek().ordinal());
                    availability.setTimeSlots(dto.getTimeSlots().stream()
                            .map(timeSlotDto -> new DonorAvailability.TimeSlot(timeSlotDto.getStartTime().toString(), timeSlotDto.getEndTime().toString()))
                            .collect(Collectors.toList()));
                    availability.setLastUpdated(new Timestamp(System.currentTimeMillis()));
                    return availability;
                })
                .collect(Collectors.toList());

        donorAvailabilityRepository.saveAll(availabilities);

        return BulkAvailabilityUpdateResponseDto.builder()
                .updatedCount(availabilities.size())
                .build();
    }

    @Override
    public void updateEmergencyAvailability(UUID donorId, EmergencyAvailabilityDto emergencyAvailabilityDto) {
        List<DonorAvailability> availabilities = donorAvailabilityRepository.findAllByDonorId(donorId);
        if (availabilities.isEmpty()) {
            throw new ResourceNotFoundException("No availability found for donor with id: " + donorId);
        }

        availabilities.forEach(availability -> {
            availability.setIsAvailableEmergency(emergencyAvailabilityDto.getIsAvailableEmergency());
            availability.setEmergencyResponsePriority(emergencyAvailabilityDto.getPriority());
            availability.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        });

        donorAvailabilityRepository.saveAll(availabilities);
    }

    @Override
    public void deleteDonorAvailability(UUID donorId, String day) {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
        List<DonorAvailability> availabilities = donorAvailabilityRepository.findAllByDonorId(donorId);
        availabilities.stream()
                .filter(a -> a.getDayOfWeek() == dayOfWeek.ordinal())
                .forEach(donorAvailabilityRepository::delete);
    }

    private DonorAvailabilityDto mapToDto(DonorAvailability availability) {
        return DonorAvailabilityDto.builder()
                .dayOfWeek(DayOfWeek.values()[availability.getDayOfWeek()])
                .timeSlots(availability.getTimeSlots().stream()
                        .map(timeSlot -> new TimeSlotDto(java.time.LocalTime.parse(timeSlot.getStartTime()), java.time.LocalTime.parse(timeSlot.getEndTime())))
                        .collect(Collectors.toList()))
                .preferredRadiusKm(availability.getPreferredRadiusKm())
                .isAvailableEmergency(availability.getIsAvailableEmergency())
                .build();
    }
}
