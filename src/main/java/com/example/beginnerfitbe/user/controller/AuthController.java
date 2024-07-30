package com.example.beginnerfitbe.user.controller;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.ResetPasswordDto;
import com.example.beginnerfitbe.user.dto.SignInReqDto;
import com.example.beginnerfitbe.user.dto.SignUpReqDto;
import com.example.beginnerfitbe.user.service.AuthService;
import com.example.beginnerfitbe.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtUtil jwtUtil;

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
    @PostMapping("/find-password")
    public ResponseEntity<?> updatePw(@RequestBody ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        String password = resetPasswordDto.getPassword();
        return ResponseEntity.ok(authService.resetPassword(email, password));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestHeader("Authorization") String refreshToken

    ) {
        // Bearer 접두사를 제거
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(authService.signOut(userId));
    }

}