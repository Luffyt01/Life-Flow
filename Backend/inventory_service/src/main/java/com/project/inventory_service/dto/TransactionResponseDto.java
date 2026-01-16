package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private UUID transactionId;
    private UUID bagId;
    private TransactionType transactionType;
    private UUID fromCenterId;
    private UUID toCenterId;
    private String notes;
    private LocalDateTime createdAt;
    private UUID requestId;
}
