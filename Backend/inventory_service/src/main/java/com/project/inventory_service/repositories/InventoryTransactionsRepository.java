package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.InventoryTransactionsEntity;
import com.project.inventory_service.entities.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionsRepository extends JpaRepository<InventoryTransactionsEntity, UUID> {
    
    List<InventoryTransactionsEntity> findByTransactionType(TransactionType type);
    
    List<InventoryTransactionsEntity> findByBag(BloodInventoryEntity bloodInventory);
    
    List<InventoryTransactionsEntity> findByRequestId(UUID requestId);
    
    @Query("SELECT t FROM InventoryTransactionsEntity t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<InventoryTransactionsEntity> findTransactionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM InventoryTransactionsEntity t WHERE t.bag.bagId = :bagId ORDER BY t.createdAt DESC")
    List<InventoryTransactionsEntity> findTransactionHistoryForBag(@Param("bagId") UUID bagId);
    
    @Query("SELECT t FROM InventoryTransactionsEntity t WHERE t.fromLocation = :location OR t.toLocation = :location")
    List<InventoryTransactionsEntity> findTransactionsByLocation(@Param("location") String location);
    
    @Query("SELECT t.transactionType, COUNT(t) FROM InventoryTransactionsEntity t WHERE t.createdAt BETWEEN :startDate AND :endDate GROUP BY t.transactionType")
    List<Object[]> countTransactionsByTypeInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM InventoryTransactionsEntity t WHERE t.performBy = :performedBy ORDER BY t.createdAt DESC")
    List<InventoryTransactionsEntity> findTransactionsByPerformer(@Param("performedBy") String performedBy);
}
