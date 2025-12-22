package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.*;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.service.impl.InventoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(bloodBagDto1, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<BagResponseDto> getBagById(@PathVariable UUID id) {
        BagResponseDto getBag = inventoryService.getBagById(id);
        return ResponseEntity.ok(getBag);
    }

    @PutMapping("{id}")
    public ResponseEntity<StringResponseDto> updateBag(@PathVariable UUID id, @RequestBody @Valid BagUpdateDto bagUpdateDto) {
        inventoryService.updateBag(id, bagUpdateDto);
        String message ="Bag is updated";
        return new ResponseEntity<>(new StringResponseDto(message),HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<List<BagResponseDto>> searchBags(@RequestParam() BloodType bloodType, @RequestParam() StatusType statusType) {
        List<BagResponseDto> bags = inventoryService.searchBags(bloodType, statusType);
        return ResponseEntity.ok(bags);
    }

    @PutMapping("/{id}/release")
    public ResponseEntity<StringResponseDto> releaseBag(@PathVariable UUID id) {
        inventoryService.releaseBag(id);
        String message ="Release blood with this id: "+id;
        return ResponseEntity.ok(new StringResponseDto(message));
    }

    @PutMapping("/{id}/update/quality_check")
    public ResponseEntity<StringResponseDto> updateQualityCheck(@PathVariable UUID id, @RequestBody UpdateQualityDto qualityDto) {
       inventoryService.updateQuality(id, qualityDto);
        String message ="Quality check updated for id: "+id;
        return ResponseEntity.ok(new StringResponseDto(message));
    }

}
