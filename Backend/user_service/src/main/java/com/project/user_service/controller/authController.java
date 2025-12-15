package com.project.user_service.controller;

import com.project.user_service.dto.*;
import com.project.user_service.service.ForgetAndResetPassService;
import com.project.user_service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    @Value("${deploy.env}")
    private String deployEnv;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final ForgetAndResetPassService forgetAndResetPassService;

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

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token, @RequestParam String email) {
        userService.verifyUser(token, email);
        return ResponseEntity.ok("User verified successfully");
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

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPasswordRequest(@RequestBody String email){
        forgetAndResetPassService.forgetPasswordRequest(email);
        return ResponseEntity.ok("Password reset email sent successfully");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam String token, @RequestBody ResetPasswordDto resetPasswordDto){
        forgetAndResetPassService.resetPasswordRequest(token, resetPasswordDto);
        return ResponseEntity.ok("Password reset successfully");

    }


}
