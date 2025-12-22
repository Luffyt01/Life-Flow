package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.entities.enums.ResolutionAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expiry_management",
        indexes = {
                @Index(name = "idx_bag_id", columnList = "bagId"),
                @Index(name = "idx_alert_level", columnList = "alertLevel"),
                @Index(name = "hospital_id_idx", columnList = "hospitalId")
        })
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ExpiryManagementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID expiryAlertId;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bag_id", nullable = false, referencedColumnName = "bagId")
    private BloodInventoryEntity bloodInventory;

    private long daysUntilExpiry;


    @Enumerated(EnumType.STRING)
    private AlertLevel alertLevel = AlertLevel.NORMAL;

//    @Column(name = "alert_sent_at")
//    private LocalDateTime alertSentAt;

    @Enumerated(EnumType.STRING)
    private ResolutionAction resolutionAction;


    private LocalDateTime resolvedAt;

    @CreationTimestamp
    @Column( updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}