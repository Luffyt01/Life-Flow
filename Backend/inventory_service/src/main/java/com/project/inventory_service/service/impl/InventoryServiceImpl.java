package com.project.inventory_service.service.impl;

import com.project.inventory_service.dto.BagResponseDto;
import com.project.inventory_service.dto.BagUpdateDto;
import com.project.inventory_service.dto.BloodBagDto;
import com.project.inventory_service.dto.UpdateQualityDto;
import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.ExpiryManagementEntity;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import com.project.inventory_service.exceptions.ExceptionTypes.ErrorInSavingDataInDatabase;
import com.project.inventory_service.exceptions.ExceptionTypes.ResourceNotFoundException;
import com.project.inventory_service.exceptions.ExceptionTypes.RuntimeConflictException;
import com.project.inventory_service.repositories.BloodInventoryRepository;
import com.project.inventory_service.repositories.ExpiryManagementRepository;
import com.project.inventory_service.service.InventoryService;
import com.project.inventory_service.utils.JwtParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final String BAG_NOT_FOUND = "Blood bag not found with id: ";

    private final ModelMapper modelMapper;
    private final BloodInventoryRepository bloodInventoryRepository;
    private final ExpiryManagementRepository expiryManagementRepository;


  
    @Override
    @Transactional
    public BloodBagDto createBloodInventory(BloodBagDto bloodBagDto) {
        log.info("Creating new blood inventory with data: {}", bloodBagDto);

        try {
            // Map DTO to entity and save to database
            BloodInventoryEntity bloodInventoryEntity = modelMapper.map(bloodBagDto, BloodInventoryEntity.class);
            log.warn("Blood inventory entity: {}", bloodInventoryEntity);
            bloodInventoryEntity.setBagId(null); // Ensure new ID is generated

            log.debug("Saving blood inventory to database");
            BloodInventoryEntity savedBloodInventory = bloodInventoryRepository.save(bloodInventoryEntity);

            log.debug("Blood inventory saved successfully with ID: {}", savedBloodInventory.getBagId());
            // Calculate and set expiry information
            long daysUntilExpiry = ChronoUnit.DAYS.between(
                    savedBloodInventory.getDonationDate(),
                    savedBloodInventory.getExpiryDate()
            );


            log.debug("Creating expiry management record for bag ID: {}", savedBloodInventory.getBagId());
            ExpiryManagementEntity expiryManagement = ExpiryManagementEntity.builder()
                    .bloodInventory(savedBloodInventory)
                    .daysUntilExpiry(daysUntilExpiry)
                    .alertLevel(AlertLevel.NORMAL)
                    .build();

            expiryManagementRepository.save(expiryManagement);

            log.info("Successfully created blood inventory with ID: {}", savedBloodInventory.getBagId());
            return modelMapper.map(savedBloodInventory, BloodBagDto.class);

        } catch (Exception e) {
            log.error("Failed to create blood inventory: {}", e.getMessage(), e);
            throw new ErrorInSavingDataInDatabase("Failed to save blood inventory: " + e.getMessage());
        }
    }

  
    @Override
    @Transactional()
    public BagResponseDto getBagById(UUID id) {
        log.info("Fetching blood bag with ID: {}", id);

        try {
            BloodInventoryEntity bloodBag = bloodInventoryRepository.findByBagId(id)
                    .orElseThrow(() -> {
                        log.warn("Blood bag not found with ID: {}", id);
                        return new ResourceNotFoundException(BAG_NOT_FOUND + id);
                    });

            log.debug("Successfully retrieved blood bag with ID: {}", id);
            return modelMapper.map(bloodBag, BagResponseDto.class);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching blood bag: {}", e.getMessage(), e);
            throw new RuntimeConflictException("Error fetching blood bag: " + e.getMessage());
        }
    }

   
    @Override
    @Transactional
    public void updateBag(UUID id, BagUpdateDto bloodBagDto) {
        log.info("Updating blood bag with ID: {}", id);

        try {
            // Verify bag exists
            if (!bloodInventoryRepository.existsByBagId(id)) {
                log.warn("Attempted to update non-existent blood bag with ID: {}", id);
                throw new ResourceNotFoundException(BAG_NOT_FOUND + id);
            }

            log.debug("Updating blood bag status and units available");
             bloodInventoryRepository.updateBag(
                    id,
                    bloodBagDto.getStatus(),
                    bloodBagDto.getUnitsAvailable()
            );

            log.info("Successfully updated blood bag with ID: {}", id);
            return ;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating blood bag with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeConflictException("Error updating blood bag: " + e.getMessage());
        }
    }
    @Override
    @Transactional()
    public List<BagResponseDto> searchBags(UUID centerId,BloodType bloodType, StatusType statusType) {
        log.info("Searching for blood bags with type: {} and status: {}", bloodType, statusType);

        try {
            List<BloodInventoryEntity> results = bloodInventoryRepository.findByBloodTypeAndStatusType(centerId,bloodType, statusType);
            log.debug("Found {} matching blood bags", results.size());
            return modelMapper.map(results, List.class);

        } catch (Exception e) {
            log.error("Error searching blood bags: {}", e.getMessage(), e);
            throw new RuntimeConflictException(
                    String.format("Error searching for blood bags with type %s and status %s", bloodType, statusType)
            );
        }
    }

    /**
     * Releases a blood bag, making it available again.
     *
     * @param id The UUID of the blood bag to release
     * @return Updated blood bag DTO
     * @throws ResourceNotFoundException if the bag is not found
     */
    @Override
    @Transactional
    public void releaseBag(UUID id) {
        log.info("Releasing blood bag with ID: {}", id);

        try {
            // Verify bag exists
            if (!bloodInventoryRepository.existsByBagId(id)) {
                log.warn("Attempted to release non-existent blood bag with ID: {}", id);
                throw new ResourceNotFoundException(BAG_NOT_FOUND + id);
            }

            log.debug("Updating blood bag status to AVAILABLE");
            bloodInventoryRepository.updateBagRelease(id, StatusType.AVAILABLE);
            log.info("Successfully released blood bag with ID: {}", id);
            return ;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error releasing blood bag with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeConflictException("Error releasing blood bag: " + e.getMessage());
        }
    }

    /**
     * Updates the quality check information for a blood bag.
     *
     * @param id The UUID of the blood bag
     * @param qualityDto DTO containing quality check information
     * @return Updated blood bag DTO
     * @throws ResourceNotFoundException if the bag is not found
     */
    @Override
    @Transactional
    public void updateQuality(UUID id, UpdateQualityDto qualityDto) {
        log.info("Updating quality check for blood bag with ID: {}", id);

        try {
            // Verify bag exists
            if (!bloodInventoryRepository.existsByBagId(id)) {
                log.warn("Attempted to update quality for non-existent blood bag with ID: {}", id);
                throw new ResourceNotFoundException(BAG_NOT_FOUND + id);
            }

            log.debug("Updating quality check information");
            bloodInventoryRepository.updateQualityCheck(
                    id,
                    qualityDto.getQualityCheckStatus(),
                    qualityDto.getQualityCheckDate(),
                    qualityDto.getQualityCheckNotes()
            );

            log.info("Successfully updated quality check for blood bag with ID: {}", id);
            return ;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating quality check for blood bag with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeConflictException("Error updating quality check: " + e.getMessage());
        }
    }
}