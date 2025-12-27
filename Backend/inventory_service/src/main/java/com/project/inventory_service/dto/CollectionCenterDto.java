package com.project.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionCenterDto {
    private UUID centerId;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private UUID hospitalId;
    private Integer capacityPerHour;
    private LocalTime operatingHoursStart;
    private LocalTime operatingHoursEnd;
    private Integer staffCount;
    private String contactNumber;
    private Boolean isActive;
    private String equipmentStatus;
}
