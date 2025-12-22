package com.project.inventory_service.service;

import com.project.inventory_service.dto.BagResponseDto;
import com.project.inventory_service.dto.BagUpdateDto;
import com.project.inventory_service.dto.BloodBagDto;
import com.project.inventory_service.dto.UpdateQualityDto;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    public BloodBagDto createBloodInventory(BloodBagDto bloodBagDto);

    BagResponseDto getBagById(UUID id);

    void updateBag(UUID id, BagUpdateDto bloodBagDto);

    List<BagResponseDto> searchBags(BloodType bloodType, StatusType statusType);

    void releaseBag(UUID id);

    void updateQuality(UUID id, UpdateQualityDto qualityDto);
}
