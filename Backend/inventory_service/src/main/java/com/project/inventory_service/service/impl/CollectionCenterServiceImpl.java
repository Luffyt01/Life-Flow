package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.CollectionCenterDto;
import com.project.inventory_service.entities.CollectionCenterEntity;
import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.CollectionCenterRepository;
import com.project.inventory_service.service.CollectionCenterService;
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
public class CollectionCenterServiceImpl implements CollectionCenterService {

    private final CollectionCenterRepository collectionCenterRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    @CacheEvict(value = "centers", allEntries = true)
    public CollectionCenterDto createCollectionCenter(CollectionCenterDto dto) {
        log.info("Creating new collection center: {}", dto.getName());
        try {
            CollectionCenterEntity entity = modelMapper.map(dto, CollectionCenterEntity.class);
            CollectionCenterEntity savedEntity = collectionCenterRepository.save(entity);
            log.info("Collection center created successfully with ID: {}", savedEntity.getCenterId());
            return modelMapper.map(savedEntity, CollectionCenterDto.class);
        } catch (Exception e) {
            log.error("Error creating collection center: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error creating collection center: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "centers", key = "#centerId")
    public CollectionCenterDto updateCollectionCenter(UUID centerId, CollectionCenterDto dto) {
        log.info("Updating collection center with ID: {}", centerId);
        try {
            CollectionCenterEntity existingEntity = collectionCenterRepository.findById(centerId)
                    .orElseThrow(() -> {
                        log.warn("Collection Center not found with ID: {}", centerId);
                        return new ResourceNotFoundException("Collection Center not found");
                    });

            existingEntity.setName(dto.getName());
            existingEntity.setAddress(dto.getAddress());
            existingEntity.setLatitude(dto.getLatitude());
            existingEntity.setLongitude(dto.getLongitude());
            existingEntity.setHospitalId(dto.getHospitalId());
            existingEntity.setCapacityPerHour(dto.getCapacityPerHour());
            existingEntity.setOperatingHoursStart(dto.getOperatingHoursStart());
            existingEntity.setOperatingHoursEnd(dto.getOperatingHoursEnd());
            existingEntity.setStaffCount(dto.getStaffCount());
            existingEntity.setContactNumber(dto.getContactNumber());
            existingEntity.setIsActive(dto.getIsActive());
            existingEntity.setEquipmentStatus(dto.getEquipmentStatus());

            CollectionCenterEntity updatedEntity = collectionCenterRepository.save(existingEntity);
            log.info("Collection center updated successfully: {}", centerId);
            return modelMapper.map(updatedEntity, CollectionCenterDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating collection center: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error updating collection center: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "centers", key = "#centerId")
    public CollectionCenterDto getCollectionCenterById(UUID centerId) {
        log.info("Fetching collection center with ID: {}", centerId);
        try {
            CollectionCenterEntity entity = collectionCenterRepository.findById(centerId)
                    .orElseThrow(() -> {
                        log.warn("Collection Center not found with ID: {}", centerId);
                        return new ResourceNotFoundException("Collection Center not found");
                    });
            return modelMapper.map(entity, CollectionCenterDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching collection center: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching collection center: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "centers", key = "'all'")
    public List<CollectionCenterDto> getAllCollectionCenters() {
        log.info("Fetching all collection centers");
        try {
            return collectionCenterRepository.findAll().stream()
                    .map((entity) -> modelMapper.map(entity, CollectionCenterDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all collection centers: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching all collection centers: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "centers", key = "#hospitalId")
    public List<CollectionCenterDto> getCollectionCentersByHospitalId(UUID hospitalId) {
        log.info("Fetching collection centers for hospital ID: {}", hospitalId);
        try {
            return collectionCenterRepository.findByHospitalId(hospitalId).stream()
                    .map((entity) -> modelMapper.map(entity, CollectionCenterDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching collection centers for hospital: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching collection centers for hospital: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "centers", key = "#centerId")
    public void deleteCollectionCenter(UUID centerId) {
        log.info("Deleting collection center with ID: {}", centerId);
        try {
            if (!collectionCenterRepository.existsById(centerId)) {
                log.warn("Collection Center not found with ID: {}", centerId);
                throw new ResourceNotFoundException("Collection Center not found");
            }
            collectionCenterRepository.deleteById(centerId);
            log.info("Collection center deleted successfully: {}", centerId);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting collection center: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error deleting collection center: " + e.getMessage());
        }
    }
}
