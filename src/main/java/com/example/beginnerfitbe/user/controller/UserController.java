package com.example.beginnerfitbe.user.controller;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.SignUpResDto;
import com.example.beginnerfitbe.user.dto.UserUpdateDto;
import com.example.beginnerfitbe.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController{

    @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회 메서드", description = "사용자 상세 정보를 조회합니다.")
    public ResponseEntity<?> read(HttpServletRequest request, @PathVariable Long id) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        if (!userId.equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.read(id));
    }

    @PostMapping("/update")
    @Operation(summary = "사용자 정보 업데이트 메서드", description = "사용자의 수정된 정보를 받아 업데이트 합니다.")
    public ResponseEntity<StateResponse> update(HttpServletRequest request, @RequestBody UserUpdateDto requestDto) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return userService.update(userId,requestDto);
    }

    @GetMapping("/emailcheck")
    @Operation(summary="이메일 중복 확인 메서드", description = "이메일을 받아 존재하는지 확인하는 메서드입니다.")
    ResponseEntity<Boolean> emailCheck(@RequestParam(value = "email") String email){
        return ResponseEntity.ok(userService.emailCheck(email));
    }
}
