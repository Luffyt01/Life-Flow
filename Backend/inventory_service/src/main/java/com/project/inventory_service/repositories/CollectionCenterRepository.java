package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.CollectionCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionCenterRepository extends JpaRepository<CollectionCenterEntity, UUID> {
    List<CollectionCenterEntity> findByHospitalId(UUID hospitalId);
    List<CollectionCenterEntity> findByIsActiveTrue();
}
