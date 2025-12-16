package com.project.user_service.repositories;

import com.project.user_service.entities.UserEntity;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.verificationToken = :token, " +
            "u.verifyTokenExpireAt = :expireAt, " +
            "u.updateAt = :updateAt " +
            "WHERE u.id = :id")
    int updateVerificationToken(
            @Param("id") UUID id,
            @Param("token") String token,
            @Param("expireAt") LocalDateTime expireAt,
            @Param("updateAt") LocalDateTime updateAt
    );



}
