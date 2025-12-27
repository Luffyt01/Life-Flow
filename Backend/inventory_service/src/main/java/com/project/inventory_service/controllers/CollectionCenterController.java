package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.CollectionCenterDto;
import com.project.inventory_service.service.CollectionCenterService;
import com.project.inventory_service.utils.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory/centers")
@RequiredArgsConstructor
public class CollectionCenterController {

    private final CollectionCenterService collectionCenterService;
    private final JwtParser jwtParser;

    @PostMapping
    public ResponseEntity<CollectionCenterDto> createCollectionCenter(HttpServletRequest req, @RequestBody CollectionCenterDto dto) {
        UUID hospitalId = jwtParser.getUserId(req);
        dto.setHospitalId(hospitalId);
        return ResponseEntity.ok(collectionCenterService.createCollectionCenter(dto));
    }

    @PutMapping("/{centerId}")
    public ResponseEntity<CollectionCenterDto> updateCollectionCenter(@PathVariable UUID centerId, @RequestBody CollectionCenterDto dto) {
        return ResponseEntity.ok(collectionCenterService.updateCollectionCenter(centerId, dto));
    }

    @GetMapping("/{centerId}")
    public ResponseEntity<CollectionCenterDto> getCollectionCenterById(@PathVariable UUID centerId) {
        return ResponseEntity.ok(collectionCenterService.getCollectionCenterById(centerId));
    }

    @GetMapping
    public ResponseEntity<List<CollectionCenterDto>> getAllCollectionCenters() {
        return ResponseEntity.ok(collectionCenterService.getAllCollectionCenters());
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<CollectionCenterDto>> getCollectionCentersByHospitalId(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(collectionCenterService.getCollectionCentersByHospitalId(hospitalId));
    }
    
    @GetMapping("/{centerId}/capacity")
    public ResponseEntity<Object> checkCenterCapacity(@PathVariable UUID centerId) {
        // Placeholder for capacity check
        // Input: center_id (path), date, time_slot
        // Output: available_slots, staff_available, equipment_status, estimated_wait_time
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{centerId}/staff")
    public ResponseEntity<Object> addStaffToCenter(@PathVariable UUID centerId) {
        // Placeholder for adding staff
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{centerId}")
    public ResponseEntity<Void> deleteCollectionCenter(@PathVariable UUID centerId) {
        collectionCenterService.deleteCollectionCenter(centerId);
        return ResponseEntity.noContent().build();
    }
}
