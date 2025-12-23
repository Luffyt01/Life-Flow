package com.project.user_service.controller;

import com.project.user_service.dto.LogInDto;
import com.project.user_service.dto.LoginResponseDto;
import com.project.user_service.dto.SignupDto;
import com.project.user_service.dto.UserDto;
import com.project.user_service.exception.ExceptionType.UserOperationException;
import com.project.user_service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    @Value("${deploy.env}")
    private String deployEnv;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUpRequest(@RequestBody @Valid SignupDto signupDto){
        UserDto userDto = userService.signUpRequest(signupDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
    public ResponseEntity<LoginResponseDto> logInRequest(@RequestBody LogInDto logInDto, HttpServletRequest request, HttpServletResponse response){

       String [] tokens = userService.logInRequest(logInDto);
       String accessToken = tokens[0];
       String refreshToken = tokens[1];

        Cookie cookie = new Cookie("refreshToken",refreshToken);

        // all cookies functionality uncomment in production
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure("production".equals(deployEnv));

        response.addCookie(cookie);
        return new ResponseEntity<>(new LoginResponseDto(accessToken),HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = userService.refreshToken(refreshToken);

        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }
}
