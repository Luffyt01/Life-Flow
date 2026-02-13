package com.project.inventory_service.controllers;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.service.impl.ExpiryManagementServiceImpl;
import com.project.inventory_service.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expiry")
@RequiredArgsConstructor
public class ExpiryManagementController {
    private final ExpiryManagementServiceImpl expiryManagementServiceImp;
    private final JwtService jwtService;

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getExpiringSoon(
            HttpServletRequest req, 
            @RequestParam(defaultValue = "7") int daysThreshold,
            @RequestParam(required = false) String bloodType,
            @RequestParam(required = false) UUID centerId) {
        
//        UUID userId = jwtParser.getUserId(req);
        // Assuming AlertLevel.WARNING or CRITICAL covers "expiring soon"
        List<ExpiryManagementTableResponseDto> searchedData = expiryManagementServiceImp.getExpiryManagementDataByAlert(centerId, AlertLevel.WARNING);
        return ResponseEntity.ok(searchedData);
    }

    @GetMapping("/expiry-alert")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getExpiryManagementDataByAlert(HttpServletRequest req, @RequestParam AlertLevel alertLevel) {
        UUID userId = jwtService.getUserId(req);
        List<ExpiryManagementTableResponseDto> searchedData = expiryManagementServiceImp.getExpiryManagementDataByAlert(userId, alertLevel);
        return ResponseEntity.ok(searchedData);
    }

    @GetMapping("/UpdateTableJob")
    public ResponseEntity<String> UpdateEveryDayExpiryManagementTableJob() {
        expiryManagementServiceImp.UpdateEveryDayExpiryManagementTableJob();

        return ResponseEntity.ok("Expiry table updated");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpiryManagementTableResponseDto>> getAllExpiryManagementTableData(HttpServletRequest req) {
        UUID hospitalId = jwtService.getUserId(req);
        List<ExpiryManagementTableResponseDto> allData = expiryManagementServiceImp.getAllExpiryManagementTableData(hospitalId);
        return ResponseEntity.ok(allData);
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Object> getInventoryDashboard(
            @RequestParam(required = false) UUID centerId,
            @RequestParam(required = false) String timePeriod) {
        // GET /inventory/dashboard
        // Input: center_id, time_period
        // Output: stock_levels, utilization_rate, wastage_rate, fulfillment_rate, top_blood_types
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/admin/reports")
    public ResponseEntity<Object> getAdminReports() {
        // GET /admin/inventory/reports
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/audit")
    public ResponseEntity<Object> createAudit() {
        // POST /admin/inventory/audit
        return ResponseEntity.ok().build();
    }

}
