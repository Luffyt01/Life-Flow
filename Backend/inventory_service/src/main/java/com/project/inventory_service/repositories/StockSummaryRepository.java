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

}
