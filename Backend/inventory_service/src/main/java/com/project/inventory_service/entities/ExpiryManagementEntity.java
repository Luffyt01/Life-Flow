package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.entities.enums.ResolutionAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "expiry_management",
        indexes = {
                @Index(name = "idx_bag_id", columnList = "bagId"),
                @Index(name = "idx_alert_level", columnList = "alertLevel")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryManagementEntity {

    @Id
    @Column(name = "expiry_alert_id", length = 50)
    private String expiryAlertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bag_id", nullable = false, referencedColumnName = "bagId")
    private BloodInventoryEntity bloodInventory;

    @Column(name = "days_until_expiry")
    private Integer daysUntilExpiry;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_level")
    private AlertLevel alertLevel = AlertLevel.WARNING;

//    @Column(name = "alert_sent_at")
//    private LocalDateTime alertSentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution_action")
    private ResolutionAction resolutionAction;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}