package com.project.Live_Flow.request_service.services.impl;

import com.project.Live_Flow.request_service.dto.AppointmentSlotDto;
import com.project.Live_Flow.request_service.dto.SlotGenerationRequestDto;
import com.project.Live_Flow.request_service.entities.AppointmentSlot;
import com.project.Live_Flow.request_service.entities.BloodRequest;
import com.project.Live_Flow.request_service.entities.enums.SlotStatus;
import com.project.Live_Flow.request_service.repositories.AppointmentSlotRepository;
import com.project.Live_Flow.request_service.repositories.BloodRequestRepository;
import com.project.Live_Flow.request_service.services.AppointmentSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Override
    public void generateSlots(UUID requestId, SlotGenerationRequestDto requestDto) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));

        List<AppointmentSlot> slots = new ArrayList<>();
        LocalDateTime slotTime = bloodRequest.getTimeSlotStart();
        while (slotTime.isBefore(bloodRequest.getTimeSlotEnd()) && slots.size() < requestDto.getMaxDonors()) {
            AppointmentSlot slot = AppointmentSlot.builder()
                    .bloodRequest(bloodRequest)
                    .collectionCenterId(bloodRequest.getCollectionCenterId())
                    .appointmentTime(slotTime)
                    .slotDuration(requestDto.getSlotDuration())
                    .status(SlotStatus.AVAILABLE)
                    .build();
            slots.add(slot);
            slotTime = slotTime.plusMinutes(requestDto.getSlotDuration());
        }
        appointmentSlotRepository.saveAll(slots);
    }

    @Override
    public List<AppointmentSlotDto> getSlotsForRequest(UUID requestId) {
        // In a real app, you'd probably want to find by request ID
        return appointmentSlotRepository.findAll().stream()
                .filter(slot -> slot.getBloodRequest().getRequestId().equals(requestId))
                .map(this::toAppointmentSlotDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSlotStatus(UUID slotId, AppointmentSlotDto updateDto) {
        AppointmentSlot slot = appointmentSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + slotId));
        slot.setStatus(updateDto.getStatus());
        appointmentSlotRepository.save(slot);
    }



    @Override
    public void bulkUpdateSlotStatus(List<AppointmentSlotDto> updateDtos) {
        List<UUID> slotIds = updateDtos.stream().map(AppointmentSlotDto::getSlotId).collect(Collectors.toList());
        List<AppointmentSlot> slots = appointmentSlotRepository.findAllById(slotIds);

        // This is a naive implementation. A more efficient approach would be to map the DTOs to the entities.
        for (AppointmentSlot slot : slots) {
            for (AppointmentSlotDto dto : updateDtos) {
                if (slot.getSlotId().equals(dto.getSlotId())) {
                    slot.setStatus(dto.getStatus());
                }
            }
        }
        appointmentSlotRepository.saveAll(slots);
    }

    private AppointmentSlotDto toAppointmentSlotDto(AppointmentSlot slot) {
        return AppointmentSlotDto.builder()
                .slotId(slot.getSlotId())
                .requestId(slot.getBloodRequest().getRequestId())
                .donorId(slot.getDonorId())
                .appointmentTime(slot.getAppointmentTime())
                .status(slot.getStatus())
                .build();
    }
}
