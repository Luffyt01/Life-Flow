package com.project.inventory_service.config;

import com.project.inventory_service.Security.CustomAccessDeniedHandler;
import com.project.inventory_service.Security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    // Public routes (e.g., Swagger, health checks)
    private static final String[] PUBLIC_ROUTES = {
            "/error",
            "/matching/swagger-ui/**",
            "/matching/v3/api-docs/**",
            "/actuator/health",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
         try{
             httpSecurity.authorizeHttpRequests((auth ->auth
                             .requestMatchers(PUBLIC_ROUTES).permitAll()
                             .anyRequest().authenticated()
                     )).
                     csrf((csrf)-> csrf.disable())
                     .sessionManagement(session ->
                             session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                     )
                     .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                     .addFilterBefore(jwtAuthFilter
                             , UsernamePasswordAuthenticationFilter.class);


                     return httpSecurity.build();
         }catch (Exception e){
             throw new RuntimeException("Failed to configure security", e);
         }
    }
}
