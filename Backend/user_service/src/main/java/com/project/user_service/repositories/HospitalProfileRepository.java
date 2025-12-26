package com.project.user_service.repositories;

import com.project.user_service.entities.HospitalProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HospitalProfileRepository extends JpaRepository<HospitalProfileEntity, UUID> {
    Optional<HospitalProfileEntity> findByUserId(UUID userId);
    Optional<HospitalProfileEntity> findByLicenseNumber(String licenseNumber);

    @Modifying
    @Transactional
    @Query("update HospitalProfileEntity  h set h.isVerified = true where h.hospitalId = :hospitalId ")
    void updateVerifyStatusById(UUID hospitalId);

    @Query("SELECT h.isVerified FROM HospitalProfileEntity h WHERE h.hospitalId = :hospitalId")
    boolean isVerified(@Param("hospitalId") UUID hospitalId);
}
