package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.service.impl.ExpiryManagementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class ExpiryManagementController {
    private final ExpiryManagementServiceImpl expiryManagementServiceImp;

    @GetMapping("/expiry-alert")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getExpiryAlert(@RequestParam AlertLevel alertLevel) {
        List<ExpiryManagementTableResponseDto> searchedData = expiryManagementServiceImp.getDataByAlertLevel(alertLevel);
        return ResponseEntity.ok(searchedData);
    }

    @GetMapping("/UpdateTableJob")
    public ResponseEntity<String> UpdateEveryDayTableJob() {
        expiryManagementServiceImp.updateExpiryStatus();

        return ResponseEntity.ok("Expiry table updated");
    }
}
