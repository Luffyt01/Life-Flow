package com.project.user_service.config;

import com.project.user_service.handler.OAuth2SuccessHandler;
import com.project.user_service.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpClient;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity

public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;


       @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter){
        httpSecurity.authorizeHttpRequests(auth ->
                auth.requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Config->
                        oauth2Config.failureUrl("/auth/login?error=true")
                                .successHandler(oAuth2SuccessHandler)
                )
        ;

        return httpSecurity.build();
    }

}
