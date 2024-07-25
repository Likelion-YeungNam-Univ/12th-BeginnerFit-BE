package com.example.beginnerfitbe.user.controller;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.HealthInfoReqDto;
import com.example.beginnerfitbe.user.dto.UserUpdateDto;
import com.example.beginnerfitbe.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController{

    @Autowired
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    @Operation(summary = "사용자 목록 조회 메서드", description = "전체 사용자를 조회합니다.")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(userService.list());
    }


    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회 메서드", description = "사용자 상세 정보를 조회합니다.")
    public ResponseEntity<?> read(HttpServletRequest request, @PathVariable Long id) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        if (!userId.equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.read(id));
    }
    @GetMapping("/me")
    @Operation(summary="나의 정보 조회 메서드", description = "현재 접속한 사용자의 정보를 조회합니다.")
    ResponseEntity<?> me(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(userService.me(userId));
    }
//    @PutMapping("")
//    @Operation(summary = "사용자 정보 업데이트 메서드", description = "사용자의 수정된 정보를 받아 업데이트 합니다.")
//    public ResponseEntity<StateResponse> update(HttpServletRequest request, @RequestPart("updateDto") UserUpdateDto requestDto, @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
//        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
//        return userService.update(userId,requestDto,profilePicture);
//    }

    @PutMapping("/health-info")
    @Operation(summary = "사용자 건강 정보 업데이트 메서드", description = "사용자의 건강 정보를 업데이트합니다.")
    public ResponseEntity<?> updateHealthInfo(@RequestBody HealthInfoReqDto reqDto) throws IOException {
        return ResponseEntity.ok(userService.updateHealthInfo(reqDto));
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "사용자 탈퇴 메서드", description = "사용자가 탈퇴하는 메서드입니다. ")
    public ResponseEntity<StateResponse> withdrawal(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return userService.withdrawal(userId);
    }

    @GetMapping("/emailcheck")
    @Operation(summary="이메일 중복 확인 메서드", description = "이메일을 받아 존재하는지 확인하는 메서드입니다.")
    ResponseEntity<Boolean> emailCheck(@RequestParam(value = "email") String email){
        return ResponseEntity.ok(userService.emailCheck(email));
    }


}
