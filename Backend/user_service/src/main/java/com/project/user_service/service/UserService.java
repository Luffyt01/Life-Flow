package com.project.user_service.service;


import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService  {


     void signUpRequest(SignupDto signupDto);
    void verifyUser(String token, String email);
    UserEntity getUserByEmail(String userEmail);
    UserEntity getUserById(String userId);
    UserDetails loadUserByUsername(String userEmail);
    UserEntity save(UserEntity newUser);


}
