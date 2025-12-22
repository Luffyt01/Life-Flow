package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class StockUpdateDto {
    @NotNull(message = "Blood type is required")
    private BloodType bloodType;
    

    private UUID hospitalId;
    
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private int quantity;
    
    private boolean isReserved;
    private boolean isExpired;
}
