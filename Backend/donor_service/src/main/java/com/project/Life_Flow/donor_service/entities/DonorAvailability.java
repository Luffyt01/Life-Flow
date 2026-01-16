package com.project.Life_Flow.donor_service.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donor_availability")
public class DonorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "availability_id")
    private UUID availabilityId;

    @Column(name = "donor_id", nullable = false)
    private UUID donorId;

    @Min(0)
    @Max(6)
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "time_slots", nullable = false, columnDefinition = "jsonb")
    private List<TimeSlot> timeSlots;

    @Min(1)
    @Max(50)
    @Column(name = "preferred_radius_km")
    private Integer preferredRadiusKm;

    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Column(name = "is_available_emergency")
    private Boolean isAvailableEmergency;

    @Min(1)
    @Max(5)
    @Column(name = "emergency_response_priority")
    private Integer emergencyResponsePriority;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "seasonality_adjustments", columnDefinition = "jsonb")
    private Map<String, Adjustment> seasonalityAdjustments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlot {
        private String startTime;
        private String endTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Adjustment {
        private Double factor;
        private String reason;
    }
}
