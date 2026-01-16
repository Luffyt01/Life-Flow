package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodComponentType;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.QualityCheckStatus;
import com.project.inventory_service.entities.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BagResponseDto {
    private UUID bagId;
    private BloodType bloodType;
    private String batchNumber;
    private LocalDate donationDate;
    private LocalDate expiryDate;
    private String storageLocation;
    private StatusType status;
    private BigDecimal currentTemperature;
    private String barcode;
    private String rfidTag;
    private UUID appointmentSlotId;
    private UUID donorId;
    private UUID collectionCenterId;
    private QualityCheckStatus qualityCheckStatus;
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;
    private Integer volumeMl;
    private BloodComponentType componentType;
    private LocalDateTime createdAt;
}
