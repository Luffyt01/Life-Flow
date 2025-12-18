package com.project.Life_Flow.donor_service.repositories;

import com.project.Life_Flow.donor_service.entities.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, UUID> {

}
