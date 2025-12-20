package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.ExpiryManagementEntity;
import com.project.inventory_service.entities.enums.AlertLevel;
import com.project.inventory_service.entities.enums.ResolutionAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpiryManagementRepository extends JpaRepository<ExpiryManagementEntity, String> {

    List<ExpiryManagementEntity> findByBloodInventory(BloodInventoryEntity bloodInventory);

    List<ExpiryManagementEntity> findByAlertLevel(AlertLevel alertLevel);
    
    List<ExpiryManagementEntity> findByResolutionActionIsNull();
    
    List<ExpiryManagementEntity> findByResolutionAction(ResolutionAction action);
    
    @Query("SELECT e FROM ExpiryManagementEntity e WHERE e.alertSentAt IS NULL AND e.resolutionAction IS NULL")
    List<ExpiryManagementEntity> findPendingAlerts();
    
    @Query("SELECT e FROM ExpiryManagementEntity e WHERE e.bloodInventory.bagId = :bagId ORDER BY e.createdAt DESC")
    List<ExpiryManagementEntity> findByBagId(@Param("bagId") UUID bagId);
    
    @Query("SELECT e FROM ExpiryManagementEntity e WHERE e.daysUntilExpiry <= :daysThreshold AND e.resolutionAction IS NULL")
    List<ExpiryManagementEntity> findExpiringSoon(@Param("daysThreshold") int daysThreshold);
    
    @Query("SELECT e.alertLevel, COUNT(e) FROM ExpiryManagementEntity e WHERE e.resolutionAction IS NULL GROUP BY e.alertLevel")
    List<Object[]> countAlertsByLevel();
    
    @Query("SELECT e FROM ExpiryManagementEntity e WHERE e.alertSentAt < :cutoffDate AND e.resolutionAction IS NULL")
    List<ExpiryManagementEntity> findUnresolvedOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}
