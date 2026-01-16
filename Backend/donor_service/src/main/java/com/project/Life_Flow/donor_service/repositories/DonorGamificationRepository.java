package com.project.Life_Flow.donor_service.repositories;

import com.project.Life_Flow.donor_service.entities.DonorGamification;
import com.project.Life_Flow.donor_service.entities.DonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonorGamificationRepository extends JpaRepository<DonorGamification, UUID> {

    Optional<DonorGamification> findByDonor(DonorProfile donor);
}
