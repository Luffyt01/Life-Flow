package com.project.user_service.service.imp;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.security.JwtService;
import com.project.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public String[] logInRequest(LogInDto logInDto ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logInDto.getEmail(), logInDto.getPassword())
        );
        if (!authentication.isAuthenticated()) {
            throw new UserOperationException("Authentication failed for user: " + logInDto.getEmail());
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new String[]{accessToken, refreshToken};
    }


    @Transactional
    @Override
    public UserDto signUpRequest(SignupDto signupDto) {

        try {

        // Here find the user is  exits or not
        UserEntity exitUser= userRepository.findByEmail(signupDto.getEmail()).orElse(null);

        if(exitUser != null){
            throw new UserOperationException("User with email " + signupDto.getEmail() + " already exists" );
        }
        UserEntity userEntity = modelMapper.map(signupDto, UserEntity.class);

        userEntity.setEmail_verified(false);
        userEntity.setProfile_complete(false);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setStatus(Status.PENDING_VERIFICATION);

        //  password hashing
        userEntity.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);
        return modelMapper.map(savedUser, UserDto.class);
        }
        catch (UserOperationException e){
            throw e;
        }
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserEntity loadUserByUsername(String userEmail) {
        return userRepository.findByEmail(userEmail).orElse(null);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }
}
