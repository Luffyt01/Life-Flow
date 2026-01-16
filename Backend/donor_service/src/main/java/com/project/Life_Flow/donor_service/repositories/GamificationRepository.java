package com.project.Life_Flow.donor_service.repositories;

import com.project.Life_Flow.donor_service.entities.Gamification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamificationRepository extends JpaRepository<Gamification, UUID> {
    Optional<Gamification> findByDonorId(UUID donorId);
}
