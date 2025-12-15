package com.project.user_service.service.imp;

import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailSendServiceImp emailSendServiceImp;



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
            String token = UUID.randomUUID().toString();

        userEntity.setEmail_verified(false);
        userEntity.setProfile_complete(false);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setStatus(Status.PENDING_VERIFICATION);
            userEntity.setVerificationToken(token);
            userEntity.setVerifyTokenExpireAt(LocalDateTime.now().plusMinutes(15));

        //  password hashing
        userEntity.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);

            emailSendServiceImp.sendVerificationEmail(userEntity.getEmail(), token);

            return modelMapper.map(savedUser, UserDto.class);
        }
        catch (UserOperationException e){
            throw e;
        }
    }

    @Override
    public void verifyUser(String token, String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserOperationException("User not found with email: " + email);
        }
        LocalDateTime now = LocalDateTime.now();
        if ((now.isAfter(user.getVerifyTokenExpireAt()))) {
            throw new TokenExpireException("VerificationToken is expired");
        }
        if (!token.equals(user.getVerificationToken())) {
            throw new TokenExpireException("VerificationToken is invalid");
        }
        user.setVerificationToken(null);
        user.setVerifyTokenExpireAt(null);
        user.setStatus(Status.ACTIVE);
        user.setEmail_verified(true);

        userRepository.save(user);
    }



    @Override
    public UserEntity getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));
    }

    @Override
    public UserEntity loadUserByUsername(String userEmail) {
        return userRepository.findByEmail(userEmail).orElse(null);
    }



    @Override
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }


}
