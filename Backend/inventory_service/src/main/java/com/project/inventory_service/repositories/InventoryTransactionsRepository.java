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
    

}
