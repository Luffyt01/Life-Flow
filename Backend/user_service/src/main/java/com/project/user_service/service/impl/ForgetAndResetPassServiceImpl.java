package com.project.user_service.service.impl;

import com.project.user_service.dto.ResetPasswordDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.ForgetPasswordRepository;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.ForgetAndResetPassService;
import com.project.user_service.service.PasswordResetTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetAndResetPassServiceImpl implements ForgetAndResetPassService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final EmailSendServiceImpl emailSendServiceImp;
    private final PasswordResetTokenService passwordResetTokenService;

    @Override
    public void forgetPasswordRequest(String email) {
        log.info("Initiating password reset request for email: {}", email);
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.warn("Password reset failed - User not found with email: {}", email);
            throw new UserOperationException("User not found with email: " + email);
        }
        
        try {
            String token = UUID.randomUUID().toString();
            log.debug("Generated password reset token for email: {}", email);
            
            passwordResetTokenService.createPasswordResetToken(email, token);
            log.debug("Password reset token created for email: {}", email);
            
            emailSendServiceImp.sendForgetPasswordEmail(email, token);
            log.info("Password reset email sent successfully to: {}", email);
            
        } catch (Exception e) {
            log.error("Error processing password reset request for email: {}", email, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void resetPasswordRequest(String token, ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        log.info("Processing password reset request for email: {}", email);
        
        if (token.isBlank()) {
            log.warn("Password reset failed - Token is blank for email: {}", email);
            throw new TokenExpireException("Token is blank");
        }
        
        log.debug("Validating password reset token for email: {}", email);
        boolean validatePasswordResetToken = passwordResetTokenService.validatePasswordResetToken(token, email);
        log.debug("Token validation result for email {}: {}", email, validatePasswordResetToken);
        
        if (!validatePasswordResetToken) {
            log.warn("Password reset failed - Invalid or expired token for email: {}", email);
            throw new TokenExpireException("Token is expired or invalid");
        }

        try {
            UserEntity user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                log.error("Password reset failed - User not found with email: {}", email);
                throw new UserOperationException("User not found with email: " + email);
            }
            
            log.debug("Updating password for user: {}", email);
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            log.info("Password updated successfully for user: {}", email);
            
            log.debug("Cleaning up password reset token for email: {}", email);
            passwordResetTokenService.deleteForgetPasswordEntity(email);
            log.debug("Password reset process completed for email: {}", email);
            
        } catch (Exception e) {
            log.error("Error during password reset for email: {}", email, e);
            throw e;
        }
    }
}
