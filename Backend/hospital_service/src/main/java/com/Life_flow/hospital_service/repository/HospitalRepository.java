package com.Life_flow.hospital_service.repository;

import com.Life_flow.hospital_service.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, UUID> {
    List<HospitalEntity> findByCity(String city);
    List<HospitalEntity> findByState(String state);
    boolean existsByHospitalNameAndCity(String hospitalName, String city);


    @Query("SELECT  h from HospitalEntity  h where h.userId = :userId")
   Optional< HospitalEntity> findByUserId(UUID userId);
}