package com.project.inventory_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
         try{
             httpSecurity.authorizeHttpRequests((auth ->
                      auth.anyRequest().permitAll()
                     )).
                     csrf((csrf)-> csrf.disable());

                     return httpSecurity.build();
         }catch (Exception e){
             throw new RuntimeException("Failed to configure security", e);
         }
    }
}
