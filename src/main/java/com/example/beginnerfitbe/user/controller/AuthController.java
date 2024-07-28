package com.example.beginnerfitbe.user.controller;

import com.example.beginnerfitbe.user.dto.SignInReqDto;
import com.example.beginnerfitbe.user.dto.SignUpReqDto;
import com.example.beginnerfitbe.user.service.AuthService;
import com.example.beginnerfitbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDto dto) throws IOException {
        userService.create(authService.signUp(dto));
        return ResponseEntity.created(null).build();
    }
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInReqDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestParam String name) {
        return ResponseEntity.ok(userService.getEmailByName(name));
    }


}