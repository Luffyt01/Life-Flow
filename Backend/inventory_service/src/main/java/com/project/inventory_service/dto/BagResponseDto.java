package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.BloodComponentType;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.QualityCheckStatus;
import com.project.inventory_service.entities.enums.StatusType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BagResponseDto {

    private UUID bagId;
    private BloodType bloodType;
    private String batchNumber;
    private LocalDate donationDate;
    private LocalDate expiryDate;
    private String storageLocation;
    private StatusType status;
    private BloodComponentType bloodComponentType;
    private Double unitsAvailable;
    private QualityCheckStatus qualityCheckStatus;
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}