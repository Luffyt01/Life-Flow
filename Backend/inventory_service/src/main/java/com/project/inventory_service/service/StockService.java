package com.project.inventory_service.service;

import com.project.inventory_service.dto.StockSummaryDto;
import com.project.inventory_service.dto.StockUpdateDto;
import com.project.inventory_service.entities.enums.BloodType;

import java.util.List;
import java.util.UUID;

public interface StockService {
    StockSummaryDto getStockSummary(BloodType bloodType, UUID hospitalId);
    List<StockSummaryDto> getStockByHospital(UUID hospitalId);
    List<StockSummaryDto> getStockByBloodType(BloodType bloodType);
    StockSummaryDto updateStock(StockUpdateDto updateDto);
    List<StockSummaryDto> getStockNeedingReorder();
    StockSummaryDto initializeStock(StockUpdateDto initDto);
}
