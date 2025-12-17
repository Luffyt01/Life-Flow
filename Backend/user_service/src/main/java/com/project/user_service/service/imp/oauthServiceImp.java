package com.project.user_service.service.imp;

import com.project.user_service.entities.UserEntity;
import com.project.user_service.entities.enums.AuthProviderType;
import com.project.user_service.entities.enums.Status;
import com.project.user_service.entities.enums.UserRole;
import com.project.user_service.exception.ExceptionType.ResourceNotFoundException;
import com.project.user_service.exception.ExceptionType.RuntimeConflictException;
import com.project.user_service.repositories.UserRepository;
import com.project.user_service.service.UserService;
import com.project.user_service.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for handling OAuth2 authentication flows.
 * Manages user creation and updates during OAuth2 login processes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class oauthServiceImp {
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Handles OAuth2 login request by either creating a new user or updating an existing one.
     * 
     * @param oAuth2User The OAuth2 user details from the provider
     * @param registrationId The registration ID of the OAuth2 client (e.g., "google", "github")
     * @return The processed UserEntity
     * @throws ResourceNotFoundException if required user attributes are missing
     */
    public UserEntity handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        log.info("Processing OAuth2 login request for provider: {}", registrationId);
        
        // Determine the provider type from registration ID
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        log.debug("Resolved provider type: {}", providerType);

        // Extract user details from OAuth2 attributes
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");
        
        if (email == null || email.isBlank()) {
            log.error("OAuth2 user email is missing for provider: {}", registrationId);
            throw new ResourceNotFoundException("Email not provided by OAuth2 provider");
        }

        log.debug("Looking up user with email: {}", email);
        UserEntity user = userRepository.findByEmail1(email).orElse(null);

        //if userProvider already exists with google than return simple
        if(user != null && user.getProvider().contains(providerType)){
            log.info("User already exists with Google provider");
            return user;
        }


        //Update existing user
        if (user != null) {
            // Existing user - update provider information
            log.info("Updating existing user with OAuth2 provider: {} for email: {}", providerType, email);
//
            List<AuthProviderType> providers = user.getProvider() != null ?
                    new ArrayList<>(user.getProvider()) : new ArrayList<>();

            if (!providers.contains(providerType)) {
                providers.add(providerType);
            }
            try {
                user.setProvider(providers);
            user.setStatus(Status.ACTIVE);
            user = userService.save(user);
//
//                userRepository.exitingUserUpate(
//                        email,
//                        providers,
//                        Status.ACTIVE,
//                        UserRole.DONOR
//                );
            }catch (Exception e){
                log.error("Failed to update existing user with OAuth2 provider: {} for email: {}", providerType, email);
                throw new RuntimeConflictException("Failed to update existing user with OAuth2 provider");
            }

            log.debug("Successfully updated user with ID: {}", user.getId());
        } else {
            // New user - create user entity
            log.info("Creating new user with OAuth2 provider: {} for email: {}", providerType, email);
            user = UserEntity.builder()
                    .email(email)
                    .fullName(fullName != null ? fullName : email.split("@")[0]) // Use email prefix if name is not available
                    .provider(List.of(providerType))
                    .status(Status.ACTIVE)
                    .role(UserRole.DONOR) // Default role for OAuth2 users
                    .email_verified(true) // OAuth2 emails are typically verified by the provider
                    .build();
            
            user = userService.save(user);
            log.info("Successfully created new user with ID: {}", user.getId());
        }
        
        return user;
    }
}
