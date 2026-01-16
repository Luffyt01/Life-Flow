package com.project.inventory_service.service;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.enums.AlertLevel;

import java.util.List;
import java.util.UUID;

public interface ExpiryManagementService {
    List<ExpiryManagementTableResponseDto> getExpiryManagementDataByAlert(UUID hospitalId,AlertLevel alertLevel);
    void UpdateEveryDayExpiryManagementTableJob();

    List<ExpiryManagementTableResponseDto> getAllExpiryManagementTableData(UUID hospitalId);
}
