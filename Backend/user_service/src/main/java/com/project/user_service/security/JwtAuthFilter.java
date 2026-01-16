package com.project.user_service.security;

import com.project.user_service.entities.UserEntity;
import com.project.user_service.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String requestUri = request.getRequestURI();
        
        logger.debug("Processing authentication for request: {}", requestUri);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.trace("No JWT token found in request headers for {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER_PREFIX.length());
        logger.trace("JWT token found in request");

        try {
            String userId = String.valueOf(jwtService.getUserIdFromToken(jwt));
            logger.debug("Successfully extracted user ID from JWT: {}", userId);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("Authenticating user with ID: {}", userId);
                
                UserEntity userDetails = userService.getUserById(userId);
                logger.debug("User details retrieved: {}", userDetails.getEmail());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Successfully authenticated user: {}", userDetails.getEmail());
            }
            
            logger.debug("Continuing filter chain for request: {}", requestUri);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (SignatureException e) {
            logger.error("JWT signature does not match: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            logger.error("Error processing JWT authentication: {}", e.getMessage(), e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}