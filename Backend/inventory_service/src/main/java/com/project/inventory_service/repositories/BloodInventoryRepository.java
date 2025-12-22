package com.project.inventory_service.repositories;

import com.project.inventory_service.dto.BagResponseDto;
import com.project.inventory_service.entities.BloodInventoryEntity;
import com.project.inventory_service.entities.enums.BloodType;
import com.project.inventory_service.entities.enums.QualityCheckStatus;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventoryEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE BloodInventoryEntity b SET b.status = :status, b.unitsAvailable = :unitsAvailable WHERE b.bagId = :id")
    void updateBag(UUID id,  StatusType status,  Double unitsAvailable);

      @Query("SELECT b from BloodInventoryEntity b where b.bagId = :id")
    Optional<BloodInventoryEntity>  findByBagId(UUID id);


      @Query("Select b from BloodInventoryEntity b where b.bloodType = :bloodType  and b.status = :statusType")
      List<BloodInventoryEntity> findByBloodTypeAndStatusType(BloodType bloodType, StatusType statusType);

       @Transactional
       @Modifying
       @Query("UPDATE BloodInventoryEntity b SET b.status = :statusType WHERE b.bagId = :id")
    void updateBagRelease(UUID id, StatusType statusType);

    @Transactional
    @Modifying
    @Query("update BloodInventoryEntity b set b.qualityCheckStatus = :qualityCheckStatus, " +
            "b.qualityCheckDate = :qualityCheckDate, " +
            "b.qualityCheckNotes = :qualityCheckNotes " +
            " where b.bagId = :id ")
    void updateQualityCheck(UUID id, QualityCheckStatus qualityCheckStatus, LocalDate qualityCheckDate, String qualityCheckNotes);

    boolean existsByBagId(UUID id);
}
