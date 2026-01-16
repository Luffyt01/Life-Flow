package com.project.Live_Flow.request_service.entities;

import com.project.Live_Flow.request_service.entities.enums.SlotStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment_slots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "slot_id")
    private UUID slotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequest bloodRequest;

    @Column(name = "donor_id")
    private UUID donorId;

    @NotNull
    @Column(name = "collection_center_id")
    private UUID collectionCenterId;

    @NotNull
    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "appointment_date", insertable = false, updatable = false)
    private LocalDate appointmentDate;

    @Column(name = "slot_duration")
    private Integer slotDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SlotStatus status;

    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;

    @Column(name = "checkout_time")
    private LocalDateTime checkoutTime;

    @Column(name = "confirmation_code", unique = true)
    private String confirmationCode;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "donor_notes")
    private String donorNotes;

    @Column(name = "staff_notes")
    private String staffNotes;

    @Column(name = "no_show")
    private Boolean noShow;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancellation_time")
    private LocalDateTime cancellationTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expected_duration_minutes")
    private Integer expectedDurationMinutes;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "travel_time_estimate_minutes")
    private Integer travelTimeEstimateMinutes;
}
