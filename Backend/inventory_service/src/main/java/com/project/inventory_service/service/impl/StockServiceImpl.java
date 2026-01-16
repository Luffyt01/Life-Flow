package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.StockSummaryDto;
import com.project.inventory_service.dto.StockUpdateDto;
import com.project.inventory_service.entities.StockSummaryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.StockSummaryRepository;
import com.project.inventory_service.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "stock", key = "{#bloodType, #hospitalId}")
    public StockSummaryDto getStockSummary(BloodType bloodType, UUID hospitalId) {
        log.info("Fetching stock summary for blood type {} at hospital {}", bloodType, hospitalId);
        try {
            StockSummaryEntity entity = stockSummaryRepository.findByBloodTypeAndCenterId(bloodType, hospitalId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Stock not found for blood type %s at hospital %s", bloodType, hospitalId)
                    ));
            return convertToDto(entity);
        } catch (ResourceNotFoundException e) {
            log.warn("Stock not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching stock summary: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching stock summary: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "stock", key = "#hospitalId")
    public List<StockSummaryDto> getStockByHospital(UUID hospitalId) {
        log.info("Fetching all stock for hospital {}", hospitalId);
        try {
            return stockSummaryRepository.findByCenterId(hospitalId).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching stock by hospital: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching stock by hospital: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "stock", key = "#bloodType")
    public List<StockSummaryDto> getStockByBloodType(BloodType bloodType) {
        log.info("Fetching all stock for blood type {}", bloodType);
        try {
            return stockSummaryRepository.findByBloodType(bloodType).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching stock by blood type: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching stock by blood type: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "stock", allEntries = true)
    public StockSummaryDto updateStock(StockUpdateDto updateDto) {
        log.info("Updating stock: {}", updateDto);
        try {
            StockSummaryEntity entity = stockSummaryRepository
                    .findByBloodTypeAndCenterId(updateDto.getBloodType(), updateDto.getCenterId())
                    .orElseGet(() -> createNewStockEntry(updateDto));
            
            if (updateDto.getThresholdUnits() != null) {
                entity.setReorderThreshold(updateDto.getThresholdUnits());
            }
            
            entity.updateStock(
                updateDto.getQuantity(),
                updateDto.isReserved(),
                updateDto.isExpired()
            );
            
            StockSummaryEntity savedEntity = stockSummaryRepository.save(entity);
            log.info("Stock updated successfully: {}", savedEntity.getStockId());
            
            return convertToDto(savedEntity);
        } catch (Exception e) {
            log.error("Error updating stock: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error updating stock: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockSummaryDto> getStockNeedingReorder() {
        log.info("Fetching all stock that needs reorder");
        try {
            return stockSummaryRepository.findByNeedsReorder(true).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching stock needing reorder: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching stock needing reorder: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "stock", allEntries = true)
    public StockSummaryDto initializeStock(StockUpdateDto initDto) {
        log.info("Initializing stock: {}", initDto);
        try {
            if (stockSummaryRepository.existsByBloodTypeAndCenterId(initDto.getBloodType(), initDto.getCenterId())) {
                throw new IllegalStateException("Stock already initialized for this blood type and hospital");
            }
            
            return updateStock(initDto);
        } catch (IllegalStateException e) {
            log.warn("Stock initialization failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error initializing stock: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error initializing stock: " + e.getMessage());
        }
    }

    private StockSummaryEntity createNewStockEntry(StockUpdateDto dto) {
        return StockSummaryEntity.builder()
                .bloodType(dto.getBloodType())
                .centerId(dto.getCenterId())
                .reorderThreshold(5) // Default threshold
                .build();
    }

    private StockSummaryDto convertToDto(StockSummaryEntity entity) {
        return modelMapper.map(entity, StockSummaryDto.class);
    }
}
