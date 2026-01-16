package com.project.user_service.service;

public interface PasswordResetTokenService {
    void createPasswordResetToken(String email, String token);

    boolean validatePasswordResetToken(String token, String email);

    void deleteForgetPasswordEntity(String email);
}
