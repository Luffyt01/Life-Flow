package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.BloodType;
import com.project.Life_Flow.donor_service.entities.enums.RecoveryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donation_history")
public class DonationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "donation_id")
    private UUID donationId;

    @Column(name = "donor_id", nullable = false)
    private UUID donorId;

    @Column(name = "appointment_slot_id", nullable = false)
    private UUID appointmentSlotId;

    @Column(name = "donation_date", nullable = false)
    private Timestamp donationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type_collected", nullable = false)
    private BloodType bloodTypeCollected;

    @DecimalMin("0.5")
    @DecimalMax("2.0")
    @Column(name = "units_collected")
    private BigDecimal unitsCollected;

    @Min(1)
    @Max(10)
    @Column(name = "health_score")
    private Integer healthScore;

    @Column(name = "staff_notes", columnDefinition = "TEXT")
    private String staffNotes;

    @Column(name = "collection_center_id", nullable = false)
    private UUID collectionCenterId;

    @Column(name = "hemoglobin_pre")
    private BigDecimal hemoglobinPre;

    @Column(name = "hemoglobin_post")
    private BigDecimal hemoglobinPost;

    @Column(name = "donation_duration_minutes")
    private Integer donationDurationMinutes;

    @Column(name = "blood_pressure_before")
    private String bloodPressureBefore;

    @Column(name = "blood_pressure_after")
    private String bloodPressureAfter;

    @Column(name = "pulse_rate_before")
    private Integer pulseRateBefore;

    @Column(name = "pulse_rate_after")
    private Integer pulseRateAfter;

    @Column(name = "weight_before")
    private BigDecimal weightBefore;

    @Column(name = "weight_after")
    private BigDecimal weightAfter;

    @Column(name = "iron_level_before")
    private BigDecimal ironLevelBefore;

    @Column(name = "iron_level_after")
    private BigDecimal ironLevelAfter;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "complications", columnDefinition = "jsonb")
    private List<Complication> complications;

    @Enumerated(EnumType.STRING)
    @Column(name = "recovery_status")
    private RecoveryStatus recoveryStatus;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Complication {
        private String type;
        private String severity;
        private String notes;
    }
}
