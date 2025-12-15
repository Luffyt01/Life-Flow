package com.project.user_service.service;

import com.project.user_service.dto.LogInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface AuthService {
    String[] logInRequest(LogInDto logInDto);

    String refreshToken(String refreshToken);
}
