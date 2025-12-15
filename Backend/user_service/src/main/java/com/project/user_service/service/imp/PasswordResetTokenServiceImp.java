package com.project.user_service.service.imp;

import com.project.user_service.entities.ForgetPasswordEntity;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.ForgetPasswordRepository;
import com.project.user_service.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImp implements PasswordResetTokenService {
    private final ForgetPasswordRepository forgetPasswordRepository;

    @Override
    public void createPasswordResetToken(String email, String token) {

        ForgetPasswordEntity forgetPasswordEntity = forgetPasswordRepository.findByEmail(email).orElse(null);
        if (forgetPasswordEntity == null) {
            ForgetPasswordEntity forgetPasswordEntity1 = ForgetPasswordEntity.builder()
                    .email(email)
                    .forget_token(token)
                    .createdAt(LocalDateTime.now())
                    .expiredAt(LocalDateTime.now().plusMinutes(15))
                    .build();
            forgetPasswordRepository.save(forgetPasswordEntity1);
        } else {
            forgetPasswordEntity.setCreatedAt(LocalDateTime.now());
            forgetPasswordEntity.setForget_token(token);
            forgetPasswordEntity.setExpiredAt(LocalDateTime.now().plusMinutes(15));
            forgetPasswordRepository.save(forgetPasswordEntity);
        }


    }

    @Override
    public boolean validatePasswordResetToken(String token, String email) {

        try {

            ForgetPasswordEntity forgetPasswordEntity = forgetPasswordRepository.findByEmail(email).orElse(null);
            if (forgetPasswordEntity == null) {
                throw new UserOperationException("ForgetPassword request not found with " + email);
            }
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(forgetPasswordEntity.getExpiredAt())) {
                throw new TokenExpireException("Token is expired");
            }
            return true;
        } catch (Exception e) {
            throw new TokenExpireException("Token is expired");
        }


    }

    public void deleteForgetPasswordEntity(String email) {
        forgetPasswordRepository.deleteByEmail(email);
    }
}
