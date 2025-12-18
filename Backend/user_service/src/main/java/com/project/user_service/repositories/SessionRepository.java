package com.project.user_service.repositories;


import com.project.user_service.entities.SessionEntity;
import com.project.user_service.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
   Optional< List<SessionEntity>> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}