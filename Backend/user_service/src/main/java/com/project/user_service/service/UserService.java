package com.project.user_service.service;


import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;

import java.util.UUID;

public interface UserService  {


     UserDto signUpRequest(SignupDto signupDto);

    void verifyUser(String token, String email);




    UserEntity getUserById(String userId);

    UserEntity loadUserByUsername(String userEmail);



    UserEntity save(UserEntity newUser);


}
