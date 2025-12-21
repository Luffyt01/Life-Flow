package com.project.inventory_service.service;

import com.project.inventory_service.dto.BagUpdateDto;
import com.project.inventory_service.dto.BloodBagDto;
import com.project.inventory_service.dto.GetBagResponseDto;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    public BloodBagDto createBloodInventory(BloodBagDto bloodBagDto);

    GetBagResponseDto getBagById(UUID id);

    GetBagResponseDto updateBag(UUID id, BagUpdateDto bloodBagDto);

    List<GetBagResponseDto> searchBags(BloodType bloodType, StatusType statusType);

    GetBagResponseDto releaseBag(UUID id);
}
