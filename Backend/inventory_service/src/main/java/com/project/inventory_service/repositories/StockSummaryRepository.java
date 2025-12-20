package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.StockSummaryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockSummaryRepository extends JpaRepository<StockSummaryEntity, UUID> {
    
    Optional<StockSummaryEntity> findByBloodType(BloodType bloodType);
    
    @Query("SELECT s FROM StockSummaryEntity s WHERE s.alertTrigger = true")
    List<StockSummaryEntity> findAlerts();
    
    @Query("SELECT s FROM StockSummaryEntity s WHERE s.availableUnits <= s.criticalThreshold")
    List<StockSummaryEntity> findCriticalStock();
    
    @Query("SELECT s FROM StockSummaryEntity s WHERE s.availableUnits <= s.recorderThreshold")
    List<StockSummaryEntity> findStockNeedingReorder();
    
    @Modifying
    @Query("UPDATE StockSummaryEntity s SET s.availableUnits = s.availableUnits + :delta WHERE s.bloodType = :bloodType")
    void updateAvailableUnits(@Param("bloodType") BloodType bloodType, @Param("delta") int delta);
    
    @Modifying
    @Query("UPDATE StockSummaryEntity s SET s.reservedUnits = s.reservedUnits + :delta WHERE s.bloodType = :bloodType")
    void updateReservedUnits(@Param("bloodType") BloodType bloodType, @Param("delta") int delta);
    
    @Query("SELECT s.bloodType, s.availableUnits, s.reservedUnits FROM StockSummaryEntity s ORDER BY s.bloodType")
    List<Object[]> getStockOverview();
    
    @Query("SELECT s FROM StockSummaryEntity s WHERE s.bloodType IN :bloodTypes")
    List<StockSummaryEntity> findByBloodTypes(@Param("bloodTypes") List<BloodType> bloodTypes);
    
    @Query("SELECT s FROM StockSummaryEntity s WHERE s.availableUnits > 0")
    List<StockSummaryEntity> findInStockSummaries();
    
    @Query("SELECT s FROM StockSummaryEntity s ORDER BY s.availableUnits ASC")
    List<StockSummaryEntity> findAllOrderByAvailability();
    
    @Query("SELECT SUM(s.availableUnits) FROM StockSummaryEntity s")
    Integer getTotalAvailableUnits();
    
    @Query("SELECT SUM(s.reservedUnits) FROM StockSummaryEntity s")
    Integer getTotalReservedUnits();
}
