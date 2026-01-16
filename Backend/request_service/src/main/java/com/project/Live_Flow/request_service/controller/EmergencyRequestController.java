package com.project.Live_Flow.request_service.controller;

import com.project.Live_Flow.request_service.dto.EmergencyRequestDto;
import com.project.Live_Flow.request_service.dto.RequestSummaryDto;
import com.project.Live_Flow.request_service.dto.RequestUpdateDto;
import com.project.Live_Flow.request_service.services.EmergencyRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/requests")
public class EmergencyRequestController {

    @Autowired
    private EmergencyRequestService emergencyRequestService;

    @PostMapping("/emergency")
    public ResponseEntity<RequestSummaryDto> createEmergencyRequest(@Valid @RequestBody EmergencyRequestDto requestDto) {
        return ResponseEntity.ok(emergencyRequestService.createEmergencyRequest(requestDto));
    }

    @GetMapping("/{request_id}")
    public ResponseEntity<RequestSummaryDto> getRequestById(@PathVariable("request_id") UUID requestId) {
        return ResponseEntity.ok(emergencyRequestService.getRequestById(requestId));
    }

    @GetMapping
    public ResponseEntity<List<RequestSummaryDto>> getRequests(
            @RequestParam(required = false) UUID hospitalId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String dateFrom) {
        return ResponseEntity.ok(emergencyRequestService.getRequests(hospitalId, status, urgency, dateFrom));
    }

    @PutMapping("/{request_id}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable("request_id") UUID requestId, @RequestBody RequestUpdateDto updateDto) {
        emergencyRequestService.cancelRequest(requestId, updateDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{request_id}/extend")
    public ResponseEntity<Void> extendRequestDeadline(@PathVariable("request_id") UUID requestId, @RequestBody RequestUpdateDto updateDto) {
        emergencyRequestService.extendRequestDeadline(requestId, updateDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> createBulkRequests(@RequestBody List<EmergencyRequestDto> requests) {
        emergencyRequestService.createBulkRequests(requests);
        return ResponseEntity.ok().build();
    }
}
