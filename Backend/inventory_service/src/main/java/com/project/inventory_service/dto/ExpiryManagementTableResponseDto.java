package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.entities.enums.ResolutionAction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpiryManagementTableResponseDto {
    private UUID expiryAlertId;
    private BagResponseDto bloodInventory;
    private Integer daysUntilExpiry;
    private AlertLevel alertLevel;
    //    private LocalDateTime alertSentAt;
    private ResolutionAction resolutionAction;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
