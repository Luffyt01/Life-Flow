package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.entities.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    @NotNull(message = "Bag ID is required")
    private UUID bagId;

    private UUID fromCenterId;
    private UUID toCenterId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    private StatusType previousStatus;
    private StatusType newStatus;

    private UUID performedBy;
    private String notes;
    
    private UUID requestId;
    private UUID hospitalId;
}
