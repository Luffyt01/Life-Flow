package com.project.Live_Flow.request_service.services;

import com.project.Live_Flow.request_service.dto.AppointmentSlotDto;
import com.project.Live_Flow.request_service.dto.SlotGenerationRequestDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentSlotService {
    void generateSlots(UUID requestId, SlotGenerationRequestDto requestDto);
    List<AppointmentSlotDto> getSlotsForRequest(UUID requestId);
    void updateSlotStatus(UUID slotId, AppointmentSlotDto updateDto);
    void bulkUpdateSlotStatus(List<AppointmentSlotDto> updateDtos);
}
