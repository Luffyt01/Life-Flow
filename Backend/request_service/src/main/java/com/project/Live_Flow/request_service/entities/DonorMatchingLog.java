package com.project.Live_Flow.request_service.entities;

import com.project.Live_Flow.request_service.entities.dto.MatchingCriteria;
import com.project.Live_Flow.request_service.entities.enums.ResponseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "donor_matching_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorMatchingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id")
    private UUID logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequest bloodRequest;

    @NotNull
    @Column(name = "donor_id")
    private UUID donorId;

    @NotNull
    @Column(name = "match_score", precision = 5, scale = 2)
    private BigDecimal matchScore;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "criteria_used", columnDefinition = "jsonb")
    private MatchingCriteria criteriaUsed;

    @Column(name = "distance_km", precision = 5, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "travel_time_minutes")
    private Integer travelTimeMinutes;

    @Column(name = "was_notified")
    private Boolean wasNotified;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status")
    private ResponseStatus responseStatus;

    @Column(name = "response_received_at")
    private LocalDateTime responseReceivedAt;

    @Column(name = "slot_assigned")
    private UUID slotAssigned;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
