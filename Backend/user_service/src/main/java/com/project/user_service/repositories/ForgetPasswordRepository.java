package com.project.user_service.repositories;

import com.project.user_service.entities.ForgetPasswordEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordEntity, UUID> {

    Optional<ForgetPasswordEntity> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("DELETE FROM ForgetPasswordEntity f WHERE f.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE ForgetPasswordEntity fp SET fp.forget_token = :token, fp.expiredAt = :expiryTime, fp.createdAt = :now WHERE fp.id = :id")
    void updateVerificationToken(UUID id, String token, LocalDateTime expiryTime, LocalDateTime now);
}
