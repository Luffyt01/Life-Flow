package com.Life_flow.hospital_service.entity;


import com.Life_flow.hospital_service.entity.enums.BloodType;
import com.Life_flow.hospital_service.entity.enums.ResponseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donor_responses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID responseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequestEntity bloodRequest;

    @Column(nullable = false)
    private UUID donorId;

    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "donor_blood_type")
    @Enumerated(EnumType.STRING)
    private BloodType donorBloodType;

    @Column(name = "donor_phone")
    private String donorPhone;

    @Column(name = "donor_latitude")
    private Double donorLatitude;

    @Column(name = "donor_longitude")
    private Double donorLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status")
    private ResponseStatus responseStatus = ResponseStatus.PENDING;

    @Column(name = "distance_to_hospital_km")
    private Double distanceToHospitalKm;

    @Column(name = "estimated_travel_time_minutes")
    private Integer estimatedTravelTimeMinutes;

    @Column(name = "can_donate_after_days")
    private Integer canDonateAfterDays; // If recently donated

    @Column(name = "preferred_donation_center_id")
    private UUID preferredDonationCenterId;

    @Column(name = "preferred_donation_date")
    private LocalDateTime preferredDonationDate;

    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @CreationTimestamp
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "match_score")
    private Integer matchScore; // 0-100 based on compatibility, distance, etc.

    @OneToOne(mappedBy = "donorResponse", cascade = CascadeType.ALL)
    private AppointmentEntity appointment;
}