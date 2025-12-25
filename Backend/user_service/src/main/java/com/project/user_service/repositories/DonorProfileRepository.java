package com.project.user_service.repositories;

import com.project.user_service.entities.DonorProfileEntity;
import com.project.user_service.entities.enums.BloodType;
import com.project.user_service.entities.enums.EligibilityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonorProfileRepository extends JpaRepository<DonorProfileEntity, UUID> {
    Optional<DonorProfileEntity> findByUserId(UUID userId);
    List<DonorProfileEntity> findByBloodType(BloodType bloodType);
    List<DonorProfileEntity> findByEligibilityStatus(EligibilityStatus status);

    @Query("SELECT d FROM DonorProfileEntity d " +
           "WHERE (:bloodType IS NULL OR d.bloodType = :bloodType) " +
           "AND (:eligibilityStatus IS NULL OR d.eligibilityStatus = :eligibilityStatus) " +
           "AND (:minWeight IS NULL OR d.weightKg >= :minWeight)")
    Page<DonorProfileEntity> searchDonors(
            @Param("bloodType") BloodType bloodType,
            @Param("eligibilityStatus") EligibilityStatus eligibilityStatus,
            @Param("minWeight") BigDecimal minWeight,
            Pageable pageable
    );
    
    // Note: 'city' search is complex without a direct city column. 
    // If you have a city column in UserEntity, you would join:
    // "AND (:city IS NULL OR d.user.address LIKE %:city%)" (assuming address contains city)
}
