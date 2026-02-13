package com.project.Live_Flow.request_service.configs;

import com.project.inventory_service.Security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
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
                     .addFilterBefore(jwtAuthFilter
                             , UsernamePasswordAuthenticationFilter.class);


                     return httpSecurity.build();
         }catch (Exception e){
             throw new RuntimeException("Failed to configure security", e);
         }
    }
}
