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

    Optional<StockSummaryEntity> findByBloodTypeAndCenterId(BloodType bloodType, UUID centerId);

    List<StockSummaryEntity> findByCenterId(UUID centerId);

    List<StockSummaryEntity> findByBloodType(BloodType bloodType);

    List<StockSummaryEntity> findByNeedsReorder(boolean needsReorder);

    @Modifying
    @Query("UPDATE StockSummaryEntity s SET " +
            "s.availableUnits = s.availableUnits + :availableChange, " +
            "s.reservedUnits = s.reservedUnits + :reservedChange, " +
            "s.expiredUnits = s.expiredUnits + :expiredChange, " +
            "s.needsReorder = (s.availableUnits + :availableChange) <= s.reorderThreshold " +
            "WHERE s.stockId = :stockId ")
    int updateStockLevels(
            @Param("stockId") UUID stockId,
            @Param("availableChange") int availableChange,
            @Param("reservedChange") int reservedChange,
            @Param("expiredChange") int expiredChange
    );


    boolean existsByBloodTypeAndCenterId(BloodType bloodType, UUID centerId);
}