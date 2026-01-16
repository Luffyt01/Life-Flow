package com.project.Live_Flow.request_service.controller;

import com.project.Live_Flow.request_service.dto.AppointmentSlotDto;
import com.project.Live_Flow.request_service.dto.SlotGenerationRequestDto;
import com.project.Live_Flow.request_service.services.AppointmentSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class AppointmentSlotController {

    @Autowired
    private AppointmentSlotService appointmentSlotService;

    @PostMapping("/requests/{request_id}/slots/generate")
    public ResponseEntity<Void> generateSlots(@PathVariable("request_id") UUID requestId, @Valid @RequestBody SlotGenerationRequestDto requestDto) {
        appointmentSlotService.generateSlots(requestId, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests/{request_id}/slots")
    public ResponseEntity<List<AppointmentSlotDto>> getSlotsForRequest(@PathVariable("request_id") UUID requestId) {
        return ResponseEntity.ok(appointmentSlotService.getSlotsForRequest(requestId));
    }

    @PutMapping("/slots/{slot_id}/status")
    public ResponseEntity<Void> updateSlotStatus(@PathVariable("slot_id") UUID slotId, @RequestBody AppointmentSlotDto updateDto) {
        appointmentSlotService.updateSlotStatus(slotId, updateDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/slots/bulk/update")
    public ResponseEntity<Void> bulkUpdateSlotStatus(@RequestBody List<AppointmentSlotDto> updateDtos) {
        appointmentSlotService.bulkUpdateSlotStatus(updateDtos);
        return ResponseEntity.ok().build();
    }
}
