package com.project.Live_Flow.request_service.services;

import com.project.Live_Flow.request_service.dto.BookAppointmentRequestDto;
import com.project.Live_Flow.request_service.dto.CheckInRequestDto;
import com.project.Live_Flow.request_service.dto.CheckOutRequestDto;

import java.util.Map;
import java.util.UUID;

public interface AppointmentBookingService {
    Map<String, String> bookAppointment(BookAppointmentRequestDto requestDto);
    void offerAppointment(Map<String, Object> payload);
    void checkIn(UUID appointmentId, CheckInRequestDto requestDto);
    void checkOut(UUID appointmentId, CheckOutRequestDto requestDto);
}
