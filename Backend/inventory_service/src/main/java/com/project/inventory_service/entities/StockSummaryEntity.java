package com.project.inventory_service.entities;


import com.project.inventory_service.entities.enums.BloodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_summary",
indexes = {
        @Index(name="blood_type_idx",columnList = "bloodType"),
        @Index(name="last_updated_idx",columnList = "lastUpdated"),
        @Index(name="alert_trigger_idx",columnList = "alertTrigger")
}
)
public class StockSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID stockId;
    @Column(nullable = false)
    private BloodType bloodType;


    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int availableUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int reservedUnits = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 5")
    private int recorderThreshold = 5;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 2")
    private int criticalThreshold = 2;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean alertTrigger = false;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    // Business logic methods
    public int getTotalUnits() {
        return availableUnits + reservedUnits;
    }

    public boolean isStockLow() {
        return availableUnits <= criticalThreshold;
    }

    public boolean needsReordering() {
        return availableUnits <= recorderThreshold;
    }

    public void updateAvailableUnits(int delta) {
        this.availableUnits = Math.max(0, this.availableUnits + delta);
        updateAlertStatus();
    }

    public void updateReservedUnits(int delta) {
        this.reservedUnits = Math.max(0, this.reservedUnits + delta);
        updateAlertStatus();
    }

    private void updateAlertStatus() {
        this.alertTrigger = isStockLow() || needsReordering();
    }

}
