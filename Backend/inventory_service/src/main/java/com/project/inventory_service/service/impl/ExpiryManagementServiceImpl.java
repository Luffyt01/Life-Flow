package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.ExpiryManagementTableResponseDto;
import com.project.inventory_service.entities.ExpiryManagementEntity;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.ExpiryManagementRepository;
import com.project.inventory_service.service.ExpiryManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiryManagementServiceImpl implements ExpiryManagementService {

    private final ExpiryManagementRepository expiryManagementRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public List<ExpiryManagementTableResponseDto> getExpiryManagementDataByAlert(UUID userId, AlertLevel alertLevel) {
        log.info("Fetching expiry management data for alert level: {}", alertLevel);

        try {
            List<ExpiryManagementEntity> getData = expiryManagementRepository.findByAlertLevel(userId,alertLevel);
            log.debug("Found {} records with alert level: {}", getData.size(), getData);
            log.info(getData.toString());
            if (getData == null || getData.isEmpty()) {
                log.info("No records found for alert level: {}", alertLevel);
                return Collections.emptyList();
            }

            log.debug("Found {} records with alert level: {}", getData.size(), alertLevel);

            return getData.stream()
                    .filter(Objects::nonNull)  // Filter out any null entries
                    .map(data -> {
                        try {
                            return modelMapper.map(data, ExpiryManagementTableResponseDto.class);
                        } catch (Exception e) {
                            log.error("Error mapping entity to DTO: {}", e.getMessage());
                            return null;  // or handle the error as needed
                        }
                    })
                    .filter(Objects::nonNull)  // Filter out any null DTOs from failed mappings
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving expiry management data for alert level {}: {}",
                    alertLevel, e.getMessage(), e);
            throw new RuntimeConflictException("Error retrieving expiry management data: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void UpdateEveryDayExpiryManagementTableJob() {
        log.info("Starting expiry status update job");

        try {
            int updatedCount = expiryManagementRepository.updateExpiryStatus();
            log.info("Successfully updated expiry status for {} blood bags", updatedCount);

        } catch (Exception e) {
            log.error("Error updating expiry status: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Failed to update expiry status: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<ExpiryManagementTableResponseDto> getAllExpiryManagementTableData(UUID hospitalId) {
        log.info("Fetching all expiry management data for hospital: {}", hospitalId);
        try{
            List<ExpiryManagementEntity> getData = expiryManagementRepository.findAllByHospitalId(hospitalId);
            return getData.stream()
                    .filter(Objects::nonNull)  // Filter out any null entries
                    .map(data -> {
                        try {
                            return modelMapper.map(data, ExpiryManagementTableResponseDto.class);
                        } catch (Exception e) {
                            log.error("Error mapping entity to DTO: {}", e.getMessage());
                            return null;  // or handle the error as needed
                        }
                    })
                    .filter(Objects::nonNull)  // Filter out any null DTOs from failed mappings
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("Error retrieving expiry management data for alert level : {}",
                    e.getMessage(), e);
            throw new RuntimeConflictException("Error retrieving expiry management data: " + e.getMessage());
        }
    }
}