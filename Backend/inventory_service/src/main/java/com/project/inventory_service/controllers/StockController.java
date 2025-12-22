package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.StockResponseDto;
import com.project.inventory_service.dto.StockSummaryDto;
import com.project.inventory_service.dto.StockUpdateDto;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.service.StockService;
import com.project.inventory_service.utils.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/inventory/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final JwtParser jwtParser;

    @GetMapping("/summary/{bloodType}")
    public ResponseEntity<StockResponseDto> getStockSummary(
            @PathVariable BloodType bloodType,
            @RequestParam UUID hospitalId) {
        log.info("Received request for stock summary - BloodType: {}, HospitalId: {}", bloodType, hospitalId);
        StockSummaryDto summary = stockService.getStockSummary(bloodType, hospitalId);
        return ResponseEntity.ok(createSuccessResponse("Stock summary retrieved successfully", summary));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<StockResponseDto> getStockByHospital(@PathVariable UUID hospitalId) {
        log.info("Received request for all stock at hospital: {}", hospitalId);
        List<StockSummaryDto> stockList = stockService.getStockByHospital(hospitalId);
        return ResponseEntity.ok(createSuccessResponse("Hospital stock retrieved successfully", stockList));
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<StockResponseDto> getStockByBloodType(@PathVariable BloodType bloodType) {
        log.info("Received request for all stock of blood type: {}", bloodType);
        List<StockSummaryDto> stockList = stockService.getStockByBloodType(bloodType);
        return ResponseEntity.ok(createSuccessResponse("Blood type stock retrieved successfully", stockList));
    }

    @PostMapping("/update")
    public ResponseEntity<StockResponseDto> updateStock(HttpServletRequest req, @Valid @RequestBody StockUpdateDto updateDto) {
        UUID userId = jwtParser.getUserId(req);
        updateDto.setHospitalId(userId);
        log.info("Received stock update request: {}", updateDto);
        StockSummaryDto updatedStock = stockService.updateStock(updateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createSuccessResponse("Stock updated successfully", updatedStock));
    }

    @GetMapping("/needs-reorder")
    public ResponseEntity<StockResponseDto> getStockNeedingReorder() {
        log.info("Received request for stock needing reorder");
        List<StockSummaryDto> stockList = stockService.getStockNeedingReorder();
        return ResponseEntity.ok(createSuccessResponse("Stock needing reorder retrieved successfully", stockList));
    }

    @PostMapping("/initialize")
    public ResponseEntity<StockResponseDto> initializeStock(HttpServletRequest req, @Valid @RequestBody StockUpdateDto initDto) {
        log.info("Received stock initialization request: {}", initDto);
        UUID userId = jwtParser.getUserId(req);
        initDto.setHospitalId(userId);
        StockSummaryDto initializedStock = stockService.initializeStock(initDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createSuccessResponse("Stock initialized successfully", initializedStock));
    }

    private StockResponseDto createSuccessResponse(String message, Object data) {
        return StockResponseDto.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}
