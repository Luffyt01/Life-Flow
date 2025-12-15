package com.project.user_service.service.imp;

import com.project.user_service.dto.ResetPasswordDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.ForgetPasswordRepository;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.ForgetAndResetPassService;
import com.project.user_service.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgetAndResetPassServiceImp implements ForgetAndResetPassService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final EmailSendServiceImp emailSendServiceImp;
    private final PasswordResetTokenService passwordResetTokenService;

    @Override
    public void forgetPasswordRequest(String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserOperationException("User not found with email: " + email);
        }
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetToken(email, token);
        emailSendServiceImp.sendForgetPasswordEmail(email, token);

    }

    @Override
    public void resetPasswordRequest(String token, ResetPasswordDto resetPasswordDto) {
        boolean validatePasswordResetToken = passwordResetTokenService.validatePasswordResetToken(token, resetPasswordDto.getEmail());
        if (!validatePasswordResetToken) {
            throw new TokenExpireException("Token is expired");
        }

        UserEntity user = userRepository.findByEmail(resetPasswordDto.getEmail()).orElse(null);
        if (user == null) {
            throw new UserOperationException("User not found with email: " + token);
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        userRepository.save(user);
        passwordResetTokenService.deleteForgetPasswordEntity(resetPasswordDto.getEmail());
    }
}
