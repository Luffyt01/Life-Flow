package com.project.user_service.controller;


import com.project.user_service.dto.UserDto;
import com.project.user_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor

public class ProfileController {

    final AuthService authService;

    @GetMapping("/get-me")
    public ResponseEntity<UserDto> getUser(HttpServletRequest req) {
        UserDto user = authService.getUser(req);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
}
