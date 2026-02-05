package com.project.user_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
<<<<<<< HEAD
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
=======
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
>>>>>>> 7000dbe5da5246742079a2e3318e50c5703ef107
        return authenticationConfiguration.getAuthenticationManager();
    }

}
