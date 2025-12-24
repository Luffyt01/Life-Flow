package com.Life_flow.hospital_service.entity.enums;

public enum AppointmentStatus {
    SCHEDULED,          // Appointment scheduled
    CONFIRMED,          // Donor confirmed
    DONOR_ARRIVED,      // Donor arrived at center
    DONATION_STARTED,   // Donation process started
    DONATION_COMPLETED, // Donation successfully completed
    CANCELLED,          // Appointment cancelled
    NO_SHOW             // Donor didn't show up
}
