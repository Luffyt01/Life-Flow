package com.project.Live_Flow.request_service.repositories;

import com.project.Live_Flow.request_service.entities.DonorMatchingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonorMatchingLogRepository extends JpaRepository<DonorMatchingLog, UUID> {
}
