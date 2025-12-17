package com.project.user_service.service.imp;

import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.AuthProviderType;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;
import com.project.user_service.exception.ExceptionType.RuntimeConflictException;
import com.project.user_service.exception.ExceptionType.TokenExpireException;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the UserService interface providing user-related operations.
 * Handles user registration, verification, and retrieval operations.
 */
@Slf4j

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailSendServiceImp emailSendServiceImp;



    /**
     * Handles user registration process including validation and email verification.
     *
     * @param signupDto The DTO containing user registration details
     * @return UserDto containing the registered user's information
     * @throws UserOperationException if a user with the given email already exists
     */
    @Transactional
    @Override
    public void signUpRequest(SignupDto signupDto) {
        log.info("Processing signup request for email: {}", signupDto.getEmail());

        try {
            // Check if user with given email already exists
            UserEntity existingUser = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

            if ((existingUser != null) && existingUser.isEmail_verified() && existingUser.getProvider().contains(AuthProviderType.GMAIL)) {
                log.warn("Signup failed - User with email {} already exists", signupDto.getEmail());
                throw new RuntimeConflictException("User with email " + signupDto.getEmail() + " already exists");
            }

            // Map DTO to entity and set up user properties
            UserEntity userEntity = modelMapper.map(signupDto, UserEntity.class);
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();

            UserEntity savedUser=null;
            if(existingUser != null && existingUser.getProvider().contains(AuthProviderType.GOOGLE)){
              log.info("User already exists with Google provider");
                userRepository.updatePassword(
                        existingUser.getId(),
                        now ,// update timestamp
                        passwordEncoder.encode(signupDto.getPassword()),
                        signupDto.getPhoneNo()
                );
                log.debug("User password updated successfully for ID: {}", existingUser.getId());
            }
            else if(existingUser != null && existingUser.getProvider().contains(AuthProviderType.GMAIL) ){
                // OPTION 1: Use custom update query (Recommended)
                log.info("User already exists with gmail provider");
                userRepository.updateVerificationToken(
                        existingUser.getId(),
                        token,
                        now.plusMinutes(15),
                        now ,// update timestamp
                        passwordEncoder.encode(signupDto.getPassword()),
                        signupDto.getPhoneNo()

                );
//                userEntity.setVerificationToken(token);
//                userEntity.setVerifyTokenExpireAt(now.plusMinutes(15));
//                userEntity.setEmail_verified(false);
//                userEntity.setProfile_complete(false);
//                userEntity.setCreatedAt(now);
//                userEntity.setStatus(Status.PENDING_VERIFICATION);
//                userEntity.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                savedUser = userRepository.findById(existingUser.getId())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found after update"));

                log.debug("User verification info updated successfully for ID: {}", savedUser.getId());
            }

            else{
                log.info("User does not exist");

                userEntity.setEmail_verified(false);
                userEntity.setProfile_complete(false);
                userEntity.setCreatedAt(now);
                userEntity.setStatus(Status.PENDING_VERIFICATION);
                userEntity.setVerificationToken(token);
                userEntity.setVerifyTokenExpireAt(now.plusMinutes(15));
                userEntity.setPassword(passwordEncoder.encode(signupDto.getPassword()));
                userEntity.setProvider(List.of(AuthProviderType.GMAIL));
                 savedUser = save(userEntity);
                log.debug("User created successfully with ID: {}", savedUser.getId());
            }

            // Set user properties

            // Save user to database

            // Send verification email
            if(existingUser ==null || !existingUser.isEmail_verified() ){

                emailSendServiceImp.sendVerificationEmail(userEntity.getEmail(), token);
                log.info("Verification email sent to: {}", userEntity.getEmail());
            }



            return ;
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifies a user's email using the provided token and email.
     *
     * @param token The verification token sent to the user's email
     * @param email The email of the user to verify
     * @throws UserOperationException if user is not found
     * @throws TokenExpireException if token is expired or invalid
     */
    @Override
    public void verifyUser(String token, String email) {
        log.info("Verifying user with email: {}", email);
        
        // Find user by email
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Verification failed - User not found with email: {}", email);
                    return new UserOperationException("User not found with email: " + email);
                });
        if(user.isEmail_verified()){
            throw new RuntimeConflictException("User already verified");

        }


        // Validate token expiration
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(user.getVerifyTokenExpireAt())) {
            log.warn("Verification failed - Token expired for user: {}", email);
            throw new TokenExpireException("Verification token has expired");
        }

        // Validate token match
        if (!token.equals(user.getVerificationToken())) {
            log.warn("Verification failed - Invalid token for user: {}", email);
            throw new TokenExpireException("Invalid verification token");
        }

        // Update user verification status
        user.setVerificationToken(null);
        user.setVerifyTokenExpireAt(null);
        user.setStatus(Status.ACTIVE);
        user.setEmail_verified(true);

         save(user);
        log.info("User verified successfully: {}", email);
    }





    /**
     * Retrieves a user by their email address.
     *
     * @param userEmail The email address of the user to find
     * @return The found UserEntity
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    @Override
    public UserEntity getUserByEmail(String userEmail) {
        log.debug("Fetching user by email: {}", userEmail);
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", userEmail);
                    return new ResourceNotFoundException("User not found with email: " + userEmail);
                });
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The UUID of the user to find
     * @return The found UserEntity
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    @Override
    public UserEntity getUserById(String userId) {
        log.debug("Fetching user by ID: {}", userId);
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
    }

    /**
     * Loads a user by username (user ID in this case) for Spring Security.
     *
//     * @param userId The user ID as a string
     * @return The found UserEntity
     * @throws ResourceNotFoundException if no user is found with the given ID
     */

//    @Override
//    public UserDetails loadUserByUsername(String userId) {
//        log.debug("Loading user by username (ID): {}", userId);
//        return userRepository.findById(UUID.fromString(userId))
//                .orElseThrow(() -> {
//                    log.error("User not found with ID: {}", userId);
//                    return new ResourceNotFoundException("User not found with id: " + userId);
//                });
//    }

    @Transactional()
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials, user not found with email: "+username));
    }


    /**
     * Saves a user entity to the database.
     *
     * @param newUser The user entity to save
     * @return The saved user entity
     */
    @Override
    public UserEntity save(UserEntity newUser) {
        log.debug("Saving user with ID: {}", newUser.getId());
        try {
            UserEntity savedUser = userRepository.save(newUser);
            log.info("User saved successfully with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }


}
