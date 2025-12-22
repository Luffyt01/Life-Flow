package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.StockSummaryDto;
import com.project.inventory_service.dto.StockUpdateDto;
import com.project.inventory_service.entities.StockSummaryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.repositories.StockSummaryRepository;
import com.project.inventory_service.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockSummaryRepository stockSummaryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public StockSummaryDto getStockSummary(BloodType bloodType, UUID hospitalId) {
        log.info("Fetching stock summary for blood type {} at hospital {}", bloodType, hospitalId);
        StockSummaryEntity entity = stockSummaryRepository.findByBloodTypeAndHospitalId(bloodType, hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Stock not found for blood type %s at hospital %s", bloodType, hospitalId)
                ));
        return convertToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockSummaryDto> getStockByHospital(UUID hospitalId) {
        log.info("Fetching all stock for hospital {}", hospitalId);
        return stockSummaryRepository.findByHospitalId(hospitalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockSummaryDto> getStockByBloodType(BloodType bloodType) {
        log.info("Fetching all stock for blood type {}", bloodType);
        return stockSummaryRepository.findByBloodType(bloodType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockSummaryDto updateStock(StockUpdateDto updateDto) {
        log.info("Updating stock: {}", updateDto);
        
        StockSummaryEntity entity = stockSummaryRepository
                .findByBloodTypeAndHospitalId(updateDto.getBloodType(), updateDto.getHospitalId())
                .orElseGet(() -> createNewStockEntry(updateDto));
        
        entity.updateStock(
            updateDto.getQuantity(),
            updateDto.isReserved(),
            updateDto.isExpired()
        );
        
        StockSummaryEntity savedEntity = stockSummaryRepository.save(entity);
        log.info("Stock updated successfully: {}", savedEntity.getStockId());
        
        return convertToDto(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockSummaryDto> getStockNeedingReorder() {
        log.info("Fetching all stock that needs reorder");
        return stockSummaryRepository.findByNeedsReorder(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockSummaryDto initializeStock(StockUpdateDto initDto) {
        log.info("Initializing stock: {}", initDto);
        
        if (stockSummaryRepository.existsByBloodTypeAndHospitalId(initDto.getBloodType(), initDto.getHospitalId())) {
            throw new IllegalStateException("Stock already initialized for this blood type and hospital");
        }
        
        return updateStock(initDto);
    }

    private StockSummaryEntity createNewStockEntry(StockUpdateDto dto) {
        return StockSummaryEntity.builder()
                .bloodType(dto.getBloodType())
                .hospitalId(dto.getHospitalId())
                .reorderThreshold(5) // Default threshold
                .build();
    }

    private StockSummaryDto convertToDto(StockSummaryEntity entity) {
        return modelMapper.map(entity, StockSummaryDto.class);
    }
}
