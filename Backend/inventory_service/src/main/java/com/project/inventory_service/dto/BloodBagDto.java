package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodComponentType;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.QualityCheckStatus;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
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
    private BloodType bloodType;

    @NotEmpty(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Donation date is required")
    private LocalDate donationDate;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotEmpty(message = "Storage location is required")
    private String storageLocation;

    @NotNull(message = "Donor ID is required")
    private UUID donorId;

    @NotNull(message = "Collection Center ID is required")
    private UUID collectionCenterId;

    private BigDecimal currentTemperature;

    @NotEmpty(message = "Barcode is required")
    private String barcode;

    private String rfidTag;

    private QualityCheckStatus qualityCheckStatus;
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;

    @NotNull(message = "Status is required")
    private StatusType status;

    @NotNull(message = "Volume is required")
    private Integer volumeMl;

    @NotNull(message = "Component type is required")
    private BloodComponentType componentType;
}
