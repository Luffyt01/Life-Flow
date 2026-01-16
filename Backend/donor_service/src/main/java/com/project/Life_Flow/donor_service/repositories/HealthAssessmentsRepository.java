package com.project.Life_Flow.donor_service.repositories;

import com.project.Life_Flow.donor_service.entities.HealthAssessments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HealthAssessmentsRepository extends JpaRepository<HealthAssessments, UUID> {
}
