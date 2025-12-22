package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodComponentType;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BloodBagDto {

    @NotNull(message = "Blood type is required")
    @NotEmpty(message = "Blood type is required")
    private BloodType bloodType;

    @NotNull(message = "Batch number is required")
    @NotEmpty(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Donation date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in yyyy-MM-dd format")
    private LocalDate donationDate;

    @NotNull(message = "Collection date is required")
    private LocalDate collectionDate;

    @NotNull(message = "Expiry date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in yyyy-MM-dd format")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotNull(message = "Storage location is required")
    @NotEmpty(message = "Storage location is required")
    private String storageLocation;

    @NotNull(message = "Donor ID is required")
    @NotEmpty(message = "Donor ID is required")
    private UUID donorId;

    private UUID hospitalId;

    @NotNull(message = "Blood component type is required")
    @NotEmpty(message = "Blood component type is required")
    private BloodComponentType bloodComponentType;

    @NotNull(message = "Units available is required")
    @NotEmpty(message = "Units available is required")
    private Double unitsAvailable;

    @NotNull(message = "Status is required")
    @NotEmpty(message = "Status is required")
    private StatusType status;

}
