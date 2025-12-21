package com.project.inventory_service.repositories;

import com.project.inventory_service.dto.GetBagResponseDto;
import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventoryEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE BloodInventoryEntity b SET b.status = :status, b.unitsAvailable = :unitsAvailable WHERE b.bagId = :id")
    BloodInventoryEntity updateBag(UUID id,  StatusType status,  Double unitsAvailable);

      @Query("SELECT b from BloodInventoryEntity b where b.bagId = :id")
    Optional<BloodInventoryEntity>  findByBagId(UUID id);


      @Query("Select b from BloodInventoryEntity b where b.bloodType =: bloodType  and b.status =: statusType")
    List<GetBagResponseDto> findByBloodTypeAndStatusType(BloodType bloodType, StatusType statusType);


       @Query("UPDATE BloodInventoryEntity b SET b.status = :status WHERE b.bagId = :id")
    BloodInventoryEntity updateBagRelease(UUID id, StatusType statusType);
}
