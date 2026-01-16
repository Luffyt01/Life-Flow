package com.project.user_service.service;

public interface EmailSendService {

    void sendVerificationEmail(String toEmail, String token);


    void sendForgetPasswordEmail(String toEmail, String token);
}
