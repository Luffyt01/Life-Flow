package com.project.user_service.handler;


import com.project.user_service.entities.UserEntity;
import com.project.user_service.security.JwtService;
import com.project.user_service.service.AuthService;
import com.project.user_service.service.UserService;
import com.project.user_service.service.imp.oauthServiceImp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final  UserService userService;
    private final JwtService jwtService;
    private final oauthServiceImp oauthServiceImp;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("OAuth2 authentication successful, processing user details...");

        // Extract OAuth2 user details
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.debug("OAuth2 user attributes: {}", attributes);

        // Validate OAuth2 user
        if (oAuth2User == null) {
            log.error("OAuth2 user is null in authentication success handler");
            throw new RuntimeException("OAuth2 authentication failed: User details not available");
        }

        String email = oAuth2User.getAttribute("email");
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        log.info("Processing OAuth2 login for email: {}, provider: {}", email, registrationId);

        // Handle OAuth2 user (create or update in database)
        UserEntity user = oauthServiceImp.handleOAuth2LoginRequest(oAuth2User, registrationId);
        log.debug("Successfully processed OAuth2 user with ID: {}", user.getId());

        // Generate JWT tokens
        log.debug("Generating JWT tokens for user: {}", user.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:3000/?token="+accessToken;

//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);

        response.sendRedirect(frontEndUrl);
    }

}

