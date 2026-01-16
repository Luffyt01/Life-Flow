package com.project.Live_Flow.request_service.controller;

import com.project.Live_Flow.request_service.dto.BookAppointmentRequestDto;
import com.project.Live_Flow.request_service.dto.CheckInRequestDto;
import com.project.Live_Flow.request_service.dto.CheckOutRequestDto;
import com.project.Live_Flow.request_service.services.AppointmentBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentBookingController {

    @Autowired
    private AppointmentBookingService appointmentBookingService;

    @PostMapping("/book")
    public ResponseEntity<Map<String, String>> bookAppointment(@Valid @RequestBody BookAppointmentRequestDto requestDto) {
        return ResponseEntity.ok(appointmentBookingService.bookAppointment(requestDto));
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> offerAppointment(@RequestBody Map<String, Object> payload) {
        appointmentBookingService.offerAppointment(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<Void> checkIn(@PathVariable("id") UUID appointmentId, @Valid @RequestBody CheckInRequestDto requestDto) {
        appointmentBookingService.checkIn(appointmentId, requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<Void> checkOut(@PathVariable("id") UUID appointmentId, @Valid @RequestBody CheckOutRequestDto requestDto) {
        appointmentBookingService.checkOut(appointmentId, requestDto);
        return ResponseEntity.ok().build();
    }
}
