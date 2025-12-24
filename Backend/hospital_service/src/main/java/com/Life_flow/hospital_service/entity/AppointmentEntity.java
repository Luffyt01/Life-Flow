package com.Life_flow.hospital_service.entity;

import com.Life_flow.hospital_service.entity.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequestEntity bloodRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private DonorResponseEntity donorResponse;

    @Column(name = "donation_center_id", nullable = false)
    private UUID donationCenterId;

    @Column(name = "donation_center_name")
    private String donationCenterName;

    @Column(name = "donation_center_address")
    private String donationCenterAddress;

    @Column(name = "center_latitude")
    private Double centerLatitude;

    @Column(name = "center_longitude")
    private Double centerLongitude;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes = 60;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus = AppointmentStatus.SCHEDULED;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "donor_arrived_at")
    private LocalDateTime donorArrivedAt;

    @Column(name = "donation_started_at")
    private LocalDateTime donationStartedAt;

    @Column(name = "donation_completed_at")
    private LocalDateTime donationCompletedAt;

    @Column(name = "blood_bag_id")
    private UUID bloodBagId; // After donation, link to inventory

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "notes", length = 1000)
    private String notes;
}