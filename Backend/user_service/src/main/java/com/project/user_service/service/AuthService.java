package com.project.user_service.service;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


public interface AuthService {
    String[] logInRequest(LogInDto logInDto);
    String refreshToken(String refreshToken);

    @Transactional
    void logout(HttpServletRequest request, HttpServletResponse response);

    UserDto getUser(HttpServletRequest req);
}
