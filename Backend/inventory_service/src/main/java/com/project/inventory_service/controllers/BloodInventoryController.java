package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.*;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.service.impl.InventoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/core")
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasRole('HOSPITAL')")
public class BloodInventoryController {

    private final InventoryServiceImpl inventoryService;
//    private final JwtParser jwtParser;

    @GetMapping("/demo")
    public String Demo(){
        return "Hello Inventory service";
    }

    @PostMapping("/blood-bag")
    public ResponseEntity<BloodBagDto> createBloodInventory(HttpServletRequest req, @RequestBody @Valid BloodBagDto bloodBagDto) {
        // Assuming collectionCenterId is passed in the DTO or derived from context if needed.
        // here we gave the collectionCenterId and hospitalId is inside of collectionCenterTable
        BloodBagDto createdBag = inventoryService.createBloodInventory(bloodBagDto);
        return new ResponseEntity<>(createdBag, HttpStatus.CREATED);
    }

    @GetMapping("/blood-bag/{bagId}")
    public ResponseEntity<BagResponseDto> getBagById(@PathVariable UUID bagId) {
        BagResponseDto getBag = inventoryService.getBagById(bagId);
        return ResponseEntity.ok(getBag);
    }

    @PutMapping("/blood-bag/{bagId}/status")
    public ResponseEntity<StringResponseDto> updateBagStatus(@PathVariable UUID bagId, @RequestBody @Valid BagUpdateDto bagUpdateDto) {
        inventoryService.updateBag(bagId, bagUpdateDto);
        String message ="Bag status updated";
        return new ResponseEntity<>(new StringResponseDto(message),HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<List<BagResponseDto>> searchBags(
            @RequestParam(required = false) BloodType bloodType, 
            @RequestParam(required = false) StatusType status,
            @RequestParam(required = false) UUID centerId) {

        List<BagResponseDto> bags = inventoryService.searchBags(centerId,bloodType, status);
        return ResponseEntity.ok(bags);
    }
    
    @GetMapping("/trace/{bagId}")
    public ResponseEntity<BagResponseDto> traceBloodUnit(@PathVariable UUID bagId) {
        BagResponseDto bag = inventoryService.getBagById(bagId);
        return ResponseEntity.ok(bag);
    }

    @PutMapping("/blood-bag/{id}/release")
    public ResponseEntity<StringResponseDto> releaseBag(@PathVariable UUID id) {
        inventoryService.releaseBag(id);
        String message ="Release blood with this id: "+id;
        return ResponseEntity.ok(new StringResponseDto(message));
    }

    @PutMapping("/blood-bag/{id}/update/quality_check")
    public ResponseEntity<StringResponseDto> updateQualityCheck(@PathVariable UUID id, @RequestBody @Valid UpdateQualityDto qualityDto) {
       inventoryService.updateQuality(id, qualityDto);
        String message ="Quality check updated for id: "+id;
        return ResponseEntity.ok(new StringResponseDto(message));
    }

}
