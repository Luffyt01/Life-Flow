package com.project.user_service.config;

import com.project.user_service.handler.OAuth2SuccessHandler;
import com.project.user_service.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpClient;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;


       @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            logger.info("Configuring security filter chain");
            
            httpSecurity.authorizeHttpRequests(auth -> {
                        logger.debug("Configuring request matchers");
                        auth.requestMatchers("/**").permitAll()
                                .anyRequest().authenticated();
                    })
                    .csrf(csrf -> {
                        logger.debug("Disabling CSRF");
                        csrf.disable();
                    })
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement(sessionConfig -> {
                        logger.debug("Setting session creation policy to STATELESS");
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    })
                    .oauth2Login(oauth2Config -> {
                        logger.debug("Configuring OAuth2 login");
                        oauth2Config.failureUrl("/auth/login?error=true")
                                .successHandler(oAuth2SuccessHandler);
                    })
            ;

            logger.info("Security filter chain configuration completed successfully");
            return httpSecurity.build();
        } catch (Exception e) {
            logger.error("Error configuring security filter chain", e);
            throw new RuntimeException("Failed to configure security", e);
        }
    }

}
