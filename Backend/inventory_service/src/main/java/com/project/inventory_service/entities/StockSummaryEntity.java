package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.BloodType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stock_summaries",
        indexes = {
                @Index(name = "blood_type_idx", columnList = "bloodType"),
                @Index(name = "hospital_id_idx", columnList = "hospitalId")
        }
)
public class StockSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID stockId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodType bloodType;

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int availableUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int reservedUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int expiredUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 5")
    private int reorderThreshold = 5;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean needsReorder = false;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    // Helper method to update stock levels
    public void updateStock(int quantity, boolean isReserved, boolean isExpired) {
        if (isExpired) {
            this.expiredUnits += quantity;
            this.availableUnits = Math.max(0, this.availableUnits - quantity);
        } else if (isReserved) {
            this.reservedUnits += quantity;
        } else {
            this.availableUnits += quantity;
            this.totalUnits += quantity;
        }
        this.needsReorder = this.availableUnits <= this.reorderThreshold;
    }
}