package com.project.user_service.repositories;

import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.AuthProviderType;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.entities.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.provider WHERE u.email = :email")
    Optional<UserEntity> findByEmail1(@Param("email") String email);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.verificationToken = :token, " +
            "u.verifyTokenExpireAt = :expireAt, " +
            "u.updateAt = :updateAt, " +
            "u.password = :password, " +
            "u.phoneNo = :phoneNo, " +
            "u.fullName = :fullName " +
            "WHERE u.id = :id")
    void updateVerificationToken(
            @Param("id") UUID id,
            @Param("token") String token,
            @Param("expireAt") LocalDateTime expireAt,
            @Param("updateAt") LocalDateTime updateAt,
            @Param("password") String password,
            @Param("phoneNo") String phoneNo,
            @Param("fullName") String fullName
    );

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.provider = :providerType, " +
            "u.status = :status, " +
            "u.role = :userRole " +
            "WHERE u.email = :email")
    void exitingUserUpdate(String email, List<AuthProviderType> providerType, Status status, UserRole userRole);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.updateAt = :updateAt, " +
            "u.password = :password, " +
            "u.phoneNo = :phoneNo " +
            "WHERE u.id = :id")
    void updatePassword(
            @Param("id") UUID id,
            @Param("updateAt") LocalDateTime updateAt,
            @Param("password") String password,
            @Param("phoneNo") String phoneNo
    );

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET " +
            "u.password = :password, " +
            "u.provider =:providers " +
            "WHERE u.id = :id")
    void updatePasswordAfterOauth2(UUID id, @Nullable String encode, List<AuthProviderType> providers);
}
