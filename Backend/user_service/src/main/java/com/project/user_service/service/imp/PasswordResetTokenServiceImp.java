package com.project.user_service.service.imp;

import com.project.user_service.entities.ForgetPasswordEntity;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.ForgetPasswordRepository;
import com.project.user_service.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetTokenServiceImp implements PasswordResetTokenService {
    private final ForgetPasswordRepository forgetPasswordRepository;

    @Override
    public void createPasswordResetToken(String email, String token) {
        log.info("Creating password reset token for email: {}", email);
        
        try {
            ForgetPasswordEntity forgetPasswordEntity = forgetPasswordRepository.findByEmail(email).orElse(null);
            
            if (forgetPasswordEntity == null) {
                log.debug("No existing token found for email: {}, creating new token", email);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiryTime = now.plusMinutes(15);
                
                ForgetPasswordEntity newToken = ForgetPasswordEntity.builder()
                        .email(email)
                        .forget_token(token)
                        .createdAt(now)
                        .expiredAt(expiryTime)
                        .build();
                        
                forgetPasswordRepository.save(newToken);
                log.info("Created new password reset token for email: {}, expires at: {}", email, expiryTime);
            } else {
                log.debug("Updating existing token for email: {}", email);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiryTime = now.plusMinutes(15);
                forgetPasswordRepository.updateVerificationToken(
                        forgetPasswordEntity.getId(),
                        token,
                        now.plusMinutes(15),
                        now // update timestamp
                );
                log.info("Updated password reset token for email: {}, new expiry: {}", email, expiryTime);
            }
        } catch (Exception e) {
            log.error("Failed to create/update password reset token for email: {}", email, e);
            throw e;
        }


    }

    @Override
    public boolean validatePasswordResetToken(String token, String email) {
        log.debug("Validating password reset token for email: {}", email);
        
        try {
            ForgetPasswordEntity forgetPasswordEntity = forgetPasswordRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("No password reset request found for email: {}", email);
                    return new UserOperationException("Password reset request not found for email: " + email);
                });
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = forgetPasswordEntity.getExpiredAt();
            
            log.debug("Token validation - Current time: {}, Token expiry time: {}", now, expiryTime);
            
            if (now.isAfter(expiryTime)) {
                log.warn("Token validation failed - Token expired for email: {}", email);
                throw new TokenExpireException("Password reset token has expired");
            }
            
            log.debug("Token validation successful for email: {}", email);
            return true;
            
        } catch (TokenExpireException e) {
            log.error("Token validation error for email: {} - {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token validation for email: {}", email, e);
            throw new TokenExpireException("Error validating password reset token");
        }
    }

    public void deleteForgetPasswordEntity(String email) {
        log.info("Deleting password reset token for email: {}", email);
        try {
            forgetPasswordRepository.deleteByEmail(email);
            log.debug("Deleted  password reset token(s) for email: {}", email);
        } catch (Exception e) {
            log.error("Failed to delete password reset token for email: {}", email, e);
            throw e;
        }
    }
}
