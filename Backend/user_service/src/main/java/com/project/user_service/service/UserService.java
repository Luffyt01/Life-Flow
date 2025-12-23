package com.project.user_service.service;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;

public interface UserService  {

    String[] logInRequest(LogInDto logInDto);
     UserDto signUpRequest(SignupDto signupDto);
     UserEntity getUserById(Long id);

    UserEntity loadUserByUsername(String userEmail);


    String refreshToken(String refreshToken);

    UserEntity save(UserEntity newUser);
}
