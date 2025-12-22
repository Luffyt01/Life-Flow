package com.project.inventory_service.service;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.enums.AlertLevel;

import java.util.List;

public interface ExpiryManagementService {
    List<ExpiryManagementTableResponseDto> getDataByAlertLevel(AlertLevel alertLevel);
    void updateExpiryStatus();
}
