package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.service.impl.ExpiryManagementServiceImpl;
import com.project.inventory_service.utils.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class ExpiryManagementController {
    private final ExpiryManagementServiceImpl expiryManagementServiceImp;
    private final JwtParser jwtParser;

    @GetMapping("/expiry-alert")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getExpiryAlert(HttpServletRequest req, @RequestParam AlertLevel alertLevel) {
        UUID userId = jwtParser.getUserId(req);
        List<ExpiryManagementTableResponseDto> searchedData = expiryManagementServiceImp.getDataByAlertLevel(userId, alertLevel);
        return ResponseEntity.ok(searchedData);
    }

    @GetMapping("/UpdateTableJob")
    public ResponseEntity<String> UpdateEveryDayTableJob() {
        expiryManagementServiceImp.updateExpiryStatus();

        return ResponseEntity.ok("Expiry table updated");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getAllExpiryData(HttpServletRequest req) {
        UUID hospitalId = jwtParser.getUserId(req);
        List<ExpiryManagementTableResponseDto> allData = expiryManagementServiceImp.getAllExpiryData(hospitalId);
        return ResponseEntity.ok(allData);
    }

}
