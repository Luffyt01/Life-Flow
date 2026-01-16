package com.project.Live_Flow.request_service.entities;

import com.project.Live_Flow.request_service.entities.enums.BloodType;
import com.project.Live_Flow.request_service.entities.enums.RequestStatus;
import com.project.Live_Flow.request_service.entities.enums.UrgencyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blood_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "request_id")
    private UUID requestId;

    @NotNull
    @Column(name = "hospital_id")
    private UUID hospitalId;

    @NotNull
    @Column(name = "collection_center_id")
    private UUID collectionCenterId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type_needed")
    private BloodType bloodTypeNeeded;

    @NotNull
    @Min(1)
    @Column(name = "units_required")
    private Integer unitsRequired;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level")
    private UrgencyLevel urgencyLevel;

    @Column(name = "patient_condition")
    private String patientCondition;

    @Column(name = "patient_age")
    private Integer patientAge;

    @Column(name = "patient_gender")
    private String patientGender;

    @Column(name = "surgery_type")
    private String surgeryType;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "ward_number")
    private String wardNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "time_slot_start")
    private LocalDateTime timeSlotStart;

    @Column(name = "time_slot_end")
    private LocalDateTime timeSlotEnd;

    @Column(name = "slot_duration")
    private Integer slotDuration;

    @Column(name = "matched_donors_count")
    private Integer matchedDonorsCount;

    @Column(name = "confirmed_appointments")
    private Integer confirmedAppointments;

    @Column(name = "units_collected")
    private Integer unitsCollected;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancelled_by_user_id")
    private UUID cancelledByUserId;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "priority_score")
    private Integer priorityScore;

    @Column(name = "estimated_completion_time")
    private LocalDateTime estimatedCompletionTime;
}
