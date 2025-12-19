package com.project.user_service.service.impl;

import com.project.user_service.entities.SessionEntity;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;


    public void generateNewSession(UserEntity user, String refreshToken) {
        log.debug("Generating new session for user: {}", user.getId());
        List<SessionEntity> userSessions = sessionRepository.findByUser(user).orElse(null);
        log.debug("User sessions: {}", userSessions);
        if (userSessions == null) {
            userSessions = new ArrayList<>();
        }
        log.debug("User sessions: {}", userSessions);
        if (userSessions != null && userSessions.size() == SESSION_LIMIT) {
            userSessions.sort(Comparator.comparing(SessionEntity::getLastUsedAt));

            SessionEntity leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }
        log.debug("User sessions: {}", userSessions);

        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        log.debug("New session: {}", newSession);
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken) {
        log.debug("Validating session for refreshToken: {}", refreshToken);
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: " + refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        log.debug("Session validated successfully");
        sessionRepository.save(session);
    }

}
