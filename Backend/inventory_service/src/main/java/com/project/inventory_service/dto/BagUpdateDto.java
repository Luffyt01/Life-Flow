package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BagUpdateDto {
    @NotNull(message = "Status is required")
    private StatusType status;
    @NotNull(message = "Units available is required")
    @NotNull(message = "Units available is required")
    private Double unitsAvailable;
}
