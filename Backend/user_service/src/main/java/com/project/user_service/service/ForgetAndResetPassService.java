package com.project.user_service.service;

import com.project.user_service.dto.ResetPasswordDto;

public interface ForgetAndResetPassService {
    void forgetPasswordRequest(String email);

    void resetPasswordRequest(String token, ResetPasswordDto resetPasswordDto);
}
