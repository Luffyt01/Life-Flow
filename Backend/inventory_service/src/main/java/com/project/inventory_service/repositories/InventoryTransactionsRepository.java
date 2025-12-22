package com.project.inventory_service.repositories;

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

    /**
     * Find all transactions for a specific blood bag
     *
     * @param bagId The ID of the blood bag
     * @return List of transactions for the specified blood bag
     */
    List<InventoryTransactionsEntity> findByBagId(UUID bagId);

    /**
     * Find all transactions of a specific type
     *
     * @param transactionType The type of transaction
     * @return List of transactions of the specified type
     */
    List<InventoryTransactionsEntity> findByTransactionType(TransactionType transactionType);

    /**
     * Find all transactions for a specific request
     *
     * @param requestId The ID of the request
     * @return List of transactions for the specified request
     */
    List<InventoryTransactionsEntity> findByRequestId(UUID requestId);

    /**
     * Find transactions within a date range
     *
     * @param startDate Start date of the range
     * @param endDate   End date of the range
     * @return List of transactions within the specified date range
     */
    @Query("SELECT t FROM InventoryTransactionsEntity t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<InventoryTransactionsEntity> findTransactionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Check if a transaction exists for a specific request and bag
     *
     * @param requestId The ID of the request
     * @param bagId     The ID of the blood bag
     * @return true if a transaction exists, false otherwise
     */
    boolean existsByRequestIdAndBagId(UUID requestId, UUID bagId);
}
