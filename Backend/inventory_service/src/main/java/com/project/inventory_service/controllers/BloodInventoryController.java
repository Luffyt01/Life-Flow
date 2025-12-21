package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.BagUpdateDto;
import com.project.inventory_service.dto.BloodBagDto;
import com.project.inventory_service.dto.GetBagResponseDto;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.service.impl.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/inventory/bags")
@RequiredArgsConstructor
@RestController
public class BloodInventoryController {

    private final InventoryServiceImpl inventoryService;


    @PostMapping()
    public ResponseEntity<BloodBagDto> createBloodInventory(@RequestBody BloodBagDto bloodBagDto){

        BloodBagDto bloodBagDto1 = inventoryService.createBloodInventory(bloodBagDto);
        return ResponseEntity.ok(bloodBagDto1);
    }

    @GetMapping("{id}")
    public ResponseEntity<GetBagResponseDto> getBagById(@PathVariable UUID id){
        GetBagResponseDto getBag = inventoryService.getBagById(id);
        return ResponseEntity.ok(getBag);
    }

    @PutMapping("{id}")
    public ResponseEntity<GetBagResponseDto> updateBag(@PathVariable UUID id, @RequestBody BagUpdateDto bagUpdateDto){
        GetBagResponseDto updatedBag = inventoryService.updateBag(id, bagUpdateDto);
        return ResponseEntity.ok(updatedBag);
    }


    @GetMapping("/search")
    public ResponseEntity<List<GetBagResponseDto>> searchBags(@RequestParam("bloodType") BloodType bloodType, @RequestParam("batchNumber")StatusType statusType){
        List<GetBagResponseDto> bags = inventoryService.searchBags(bloodType, statusType);
        return ResponseEntity.ok(bags);
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<GetBagResponseDto> releaseBag(@PathVariable UUID id){
        GetBagResponseDto releasedBag = inventoryService.releaseBag(id);
        return ResponseEntity.ok(releasedBag);
    }
}
