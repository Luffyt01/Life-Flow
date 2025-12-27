package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockSummaryDto {
    private UUID stockId;
    private BloodType bloodType;
    private UUID centerId;
    private int totalUnits;
    private int availableUnits;
    private int reservedUnits;
    private int expiredUnits;
    private int reorderThreshold;
    private boolean needsReorder;
    private LocalDateTime lastUpdated;
}
