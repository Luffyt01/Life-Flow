package com.project.Live_Flow.request_service.services.impl;

import com.project.Live_Flow.request_service.repositories.AppointmentReminderRepository;
import com.project.Live_Flow.request_service.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private AppointmentReminderRepository reminderRepository;

    @Override
    public void scheduleReminder(Map<String, Object> payload) {
        // Logic to create and save an AppointmentReminder entity.
        // This would likely be triggered after an appointment is booked.
        // A background job would then query for pending reminders and send them.
        System.out.println("Scheduling reminder with payload: " + payload);

        // Example of creating a reminder entity (adapt payload structure as needed)
        /*
        AppointmentReminder reminder = AppointmentReminder.builder()
                .appointmentSlot(...) // Fetch the slot
                .donorId(...)
                .reminderType(ReminderType.valueOf((String) payload.get("type")))
                .channel(NotificationChannel.valueOf((String) payload.get("channel")))
                .scheduledTime(...) // Calculate based on appointment time
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();
        reminderRepository.save(reminder);
        */
    }

    @Override
    public void sendReminderNow(Map<String, Object> payload) {
        // Logic to send a reminder immediately, bypassing the scheduled time.
        // This could be used for ad-hoc notifications.
        System.out.println("Sending reminder now with payload: " + payload);
    }
}
