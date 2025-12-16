package com.project.user_service.service.imp;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.security.JwtService;
import com.project.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Configuration
@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public String[] logInRequest(LogInDto logInDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInDto.getEmail(), logInDto.getPassword())
        );
        if (!authentication.isAuthenticated()) {
            throw new UserOperationException("Authentication failed for user: " + logInDto.getEmail());
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new String[]{accessToken, refreshToken};
    }
    @Override
    public String refreshToken(String refreshToken) {
        String userId = jwtService.getUserIdFromToken(refreshToken);
        UserEntity user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }

}
