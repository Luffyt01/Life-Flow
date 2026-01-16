package com.project.Live_Flow.request_service.services.impl;

import com.project.Live_Flow.request_service.dto.BookAppointmentRequestDto;
import com.project.Live_Flow.request_service.dto.CheckInRequestDto;
import com.project.Live_Flow.request_service.dto.CheckOutRequestDto;
import com.project.Live_Flow.request_service.entities.AppointmentSlot;
import com.project.Live_Flow.request_service.entities.BloodRequest;
import com.project.Live_Flow.request_service.entities.enums.SlotStatus;
import com.project.Live_Flow.request_service.repositories.AppointmentSlotRepository;
import com.project.Live_Flow.request_service.repositories.BloodRequestRepository;
import com.project.Live_Flow.request_service.services.AppointmentBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class AppointmentBookingServiceImpl implements AppointmentBookingService {

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Override
    @Transactional
    public Map<String, String> bookAppointment(BookAppointmentRequestDto requestDto) {
        AppointmentSlot slot = appointmentSlotRepository.findById(requestDto.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + requestDto.getSlotId()));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("Slot is not available for booking.");
        }

        BloodRequest bloodRequest = bloodRequestRepository.findById(requestDto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestDto.getRequestId()));

        slot.setDonorId(requestDto.getDonorId());
        slot.setStatus(SlotStatus.CONFIRMED);
        String confirmationCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        slot.setConfirmationCode(confirmationCode);
        slot.setQrCodeUrl("/generate/qr?code=" + confirmationCode); // Example URL
        appointmentSlotRepository.save(slot);

        bloodRequest.setConfirmedAppointments(bloodRequest.getConfirmedAppointments() + 1);
        bloodRequestRepository.save(bloodRequest);

        return Map.of(
                "confirmation_code", confirmationCode,
                "qr_url", slot.getQrCodeUrl()
        );
    }

    @Override
    public void offerAppointment(Map<String, Object> payload) {
        // Business logic for offering an appointment to a donor.
        // This could involve sending a notification and setting an expiry time for the offer.
    }

    @Override
    @Transactional
    public void checkIn(UUID appointmentId, CheckInRequestDto requestDto) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        if (!slot.getConfirmationCode().equals(requestDto.getConfirmationCode())) {
            throw new IllegalArgumentException("Invalid confirmation code.");
        }

        slot.setCheckinTime(LocalDateTime.now());
        appointmentSlotRepository.save(slot);
    }

    @Override
    @Transactional
    public void checkOut(UUID appointmentId, CheckOutRequestDto requestDto) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        slot.setCheckoutTime(LocalDateTime.now());
        slot.setStaffNotes(requestDto.getStaffNotes());

        if (requestDto.getDonationSuccessful()) {
            slot.setStatus(SlotStatus.COMPLETED);

            BloodRequest bloodRequest = slot.getBloodRequest();
            bloodRequest.setUnitsCollected(bloodRequest.getUnitsCollected() + 1); // Assuming 1 unit per successful donation
            bloodRequestRepository.save(bloodRequest);
        } else {
            slot.setStatus(SlotStatus.CANCELLED); // Or another status representing an incomplete donation
        }

        appointmentSlotRepository.save(slot);
    }
}
