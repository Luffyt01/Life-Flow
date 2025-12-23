package com.project.Life_Flow.donor_service.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtParser {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public SecretKey getJwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken (){
        return Jwts.builder()
                .subject("6519660b-8855-404d-8ceb-b0890c5abe2f")
                .claim("email","rockysheoran72@gmail.com")
                .claim("role","DONOR")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 10 hours
                .signWith(getJwtSecretKey())
                .compact();
    }

    public String getUserIdFromToken(String token){
//        log.warn(generateAccessToken());
        Claims claims = Jwts.parser()
                .verifyWith(getJwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getJwtSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class); // Extract role claim
    }

    public UUID getUserId(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return UUID.fromString(getUserIdFromToken(token));
    }
}
