package com.Life_flow.hospital_service.entity;


import com.Life_flow.hospital_service.entity.enums.BloodType;
import com.Life_flow.hospital_service.entity.enums.Gender;
import com.Life_flow.hospital_service.entity.enums.RequestStatus;
import com.Life_flow.hospital_service.entity.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "blood_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestId;

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "hospital_address")
    private String hospitalAddress;

    @Column(name = "hospital_latitude")
    private Double hospitalLatitude;

    @Column(name = "hospital_longitude")
    private Double hospitalLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type_needed", nullable = false)
    private BloodType bloodTypeNeeded;

    @Column(name = "units_required", nullable = false)
    private Double unitsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level")
    private UrgencyLevel urgencyLevel = UrgencyLevel.MEDIUM;

    @Column(name = "patient_age")
    private Integer patientAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_gender")
    private Gender patientGender;

    @Column(name = "patient_condition", length = 500)
    private String patientCondition;

    @Column(name = "procedure_type")
    private String procedureType;

    @Column(name = "required_by")
    private LocalDateTime requiredBy;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "search_radius_km")
    private Integer searchRadiusKm = 50; // Default 50km

    @Column(name = "preferred_donation_centers", length = 1000)
    private String preferredDonationCenters; // JSON array of center IDs

    @Column(length = 1000)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToMany(mappedBy = "bloodRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonorResponseEntity> donorResponses = new ArrayList<>();

    @OneToMany(mappedBy = "bloodRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentEntity> appointments = new ArrayList<>();
}