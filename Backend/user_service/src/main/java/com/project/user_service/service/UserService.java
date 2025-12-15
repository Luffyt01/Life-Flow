package com.project.user_service.service;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;

import java.util.UUID;

public interface UserService  {

    String[] logInRequest(LogInDto logInDto);
     UserDto signUpRequest(SignupDto signupDto);

    void verifyUser(String token, String email);

    UserEntity getUserById(UUID id);

    UserEntity loadUserByUsername(String userEmail);


    String refreshToken(String refreshToken);

    UserEntity save(UserEntity newUser);


}
