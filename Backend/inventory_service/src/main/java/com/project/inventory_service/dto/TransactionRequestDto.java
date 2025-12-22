package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionRequestDto {
    @NotNull(message = "Bag ID is required")
    private UUID bagId;


    private UUID hospitalId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;
    
    @Positive(message = "Quantity must be a positive number")
    private int quantity;
    
    @NotBlank(message = "Source location is required")
    private String fromLocation;
    
    @NotBlank(message = "Destination location is required")
    private String toLocation;

    private String notes;
    
    private UUID requestId;
}
