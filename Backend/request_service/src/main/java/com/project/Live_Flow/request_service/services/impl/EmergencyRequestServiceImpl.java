package com.project.Live_Flow.request_service.services.impl;

import com.project.Live_Flow.request_service.dto.EmergencyRequestDto;
import com.project.Live_Flow.request_service.dto.RequestSummaryDto;
import com.project.Live_Flow.request_service.dto.RequestUpdateDto;
import com.project.Live_Flow.request_service.entities.BloodRequest;
import com.project.Live_Flow.request_service.entities.enums.RequestStatus;
import com.project.Live_Flow.request_service.repositories.BloodRequestRepository;
import com.project.Live_Flow.request_service.services.EmergencyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Override
    public RequestSummaryDto createEmergencyRequest(EmergencyRequestDto requestDto) {
        BloodRequest bloodRequest = BloodRequest.builder()
                .hospitalId(requestDto.getHospitalId())
                .collectionCenterId(requestDto.getCollectionCenterId())
                .bloodTypeNeeded(requestDto.getBloodTypeNeeded())
                .unitsRequired(requestDto.getUnitsRequired())
                .urgencyLevel(requestDto.getUrgencyLevel())
                .deadline(requestDto.getDeadline())
                .patientCondition(requestDto.getPatientCondition())
                .patientAge(requestDto.getPatientAge())
                .patientGender(requestDto.getPatientGender())
                .surgeryType(requestDto.getSurgeryType())
                .doctorName(requestDto.getDoctorName())
                .wardNumber(requestDto.getWardNumber())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        BloodRequest savedRequest = bloodRequestRepository.save(bloodRequest);
        return toRequestSummaryDto(savedRequest);
    }

    @Override
    public RequestSummaryDto getRequestById(UUID requestId) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        return toRequestSummaryDto(bloodRequest);
    }

    @Override
    public List<RequestSummaryDto> getRequests(UUID hospitalId, String status, String urgency, String dateFrom) {
        // In a real application, you would use Specifications or QueryDSL for dynamic queries
        return bloodRequestRepository.findAll().stream()
                .map(this::toRequestSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelRequest(UUID requestId, RequestUpdateDto updateDto) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        bloodRequest.setStatus(RequestStatus.CANCELLED);
        bloodRequest.setCancellationReason(updateDto.getCancellationReason());
        bloodRequest.setCancelledAt(LocalDateTime.now());
        bloodRequestRepository.save(bloodRequest);
    }

    @Override
    public void extendRequestDeadline(UUID requestId, RequestUpdateDto updateDto) {
        // This is a simplified implementation. A real one would have more logic.
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        // bloodRequest.setDeadline(updateDto.getNewDeadline());
        bloodRequestRepository.save(bloodRequest);
    }

    @Override
    public void createBulkRequests(List<EmergencyRequestDto> requests) {
        List<BloodRequest> bloodRequests = requests.stream().map(requestDto -> BloodRequest.builder()
                .hospitalId(requestDto.getHospitalId())
                .collectionCenterId(requestDto.getCollectionCenterId())
                .bloodTypeNeeded(requestDto.getBloodTypeNeeded())
                .unitsRequired(requestDto.getUnitsRequired())
                .urgencyLevel(requestDto.getUrgencyLevel())
                .deadline(requestDto.getDeadline())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build()).collect(Collectors.toList());
        bloodRequestRepository.saveAll(bloodRequests);
    }

    private RequestSummaryDto toRequestSummaryDto(BloodRequest bloodRequest) {
        return RequestSummaryDto.builder()
                .requestId(bloodRequest.getRequestId())
                .hospitalId(bloodRequest.getHospitalId())
                .urgencyLevel(bloodRequest.getUrgencyLevel())
                .status(bloodRequest.getStatus())
                .createdAt(bloodRequest.getCreatedAt())
                .deadline(bloodRequest.getDeadline())
                .unitsRequired(bloodRequest.getUnitsRequired())
                .unitsCollected(bloodRequest.getUnitsCollected())
                .build();
    }
}
