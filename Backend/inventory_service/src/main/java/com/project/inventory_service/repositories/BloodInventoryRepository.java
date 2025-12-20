package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventoryEntity, UUID> {
    
    List<BloodInventoryEntity> findByBloodType(BloodType bloodType);
    
    List<BloodInventoryEntity> findByStatus(StatusType status);
    
    List<BloodInventoryEntity> findByExpiryDateBefore(LocalDate date);
    
    List<BloodInventoryEntity> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT b FROM BloodInventoryEntity b WHERE b.bloodType = :bloodType AND b.status = 'AVAILABLE' AND b.expiryDate > CURRENT_DATE")
    List<BloodInventoryEntity> findAvailableByBloodType(@Param("bloodType") BloodType bloodType);
    
    @Query("SELECT b FROM BloodInventoryEntity b WHERE b.bloodComponentType = :componentType AND b.status = 'AVAILABLE'")
    List<BloodInventoryEntity> findAvailableByComponentType(@Param("componentType") String componentType);
    
    List<BloodInventoryEntity> findByDonorId(UUID donorId);
    
    @Query("SELECT b FROM BloodInventoryEntity b WHERE b.qualityCheckStatus = 'PENDING'")
    List<BloodInventoryEntity> findPendingQualityCheck();
    
    @Query("SELECT b.bloodType, COUNT(b) FROM BloodInventoryEntity b WHERE b.status = 'AVAILABLE' GROUP BY b.bloodType")
    List<Object[]> countAvailableByBloodType();
}
