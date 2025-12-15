package com.project.user_service.repositories;

import com.project.user_service.entities.ForgetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordEntity, UUID> {

    Optional<ForgetPasswordEntity> findByEmail(String email);


    void deleteByEmail(String email);
}
