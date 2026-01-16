package com.project.Life_Flow.donor_service.repositories;

import com.project.Life_Flow.donor_service.entities.DonorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonorAvailabilityRepository extends JpaRepository<DonorAvailability, UUID> {
    Optional<DonorAvailability> findByDonorId(UUID donorId);
    List<DonorAvailability> findAllByDonorId(UUID donorId);
}
