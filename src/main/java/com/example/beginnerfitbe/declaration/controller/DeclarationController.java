package com.example.beginnerfitbe.declaration.controller;

import com.example.beginnerfitbe.declaration.dto.DeclarationDto;
import com.example.beginnerfitbe.declaration.dto.DeclarationReqDto;
import com.example.beginnerfitbe.declaration.service.DeclarationService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class DeclarationController {

    private final DeclarationService declarationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{postId}/declarations")
    @Operation(summary = "게시글 신고 메서드", description = "게시글을 신고합니다.")
    public ResponseEntity<String> create(HttpServletRequest request, @PathVariable Long postId, @RequestBody DeclarationReqDto declarationReqDto) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(declarationService.create(userId, postId, declarationReqDto));
    }

    @GetMapping("/declarations")
    @Operation(summary = "신고 목록 조회 메서드", description = "전체 신고 목록을 조회합니다.")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(declarationService.list());
    }

    @GetMapping("/{postId}/declarations")
    @Operation(summary = "게시글 별 신고 목록 조회 메서드", description = "게시글 별 신고 목록을 조회합니다.")
    public ResponseEntity<?> getDeclarationsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(declarationService.getDeclarationsByPost(postId));
    }

    @DeleteMapping("/{postId}/declarations")
    @Operation(summary = "게시글 신고 취소 메서드", description = "게시글을 신고를 취소 합니다.")
    public ResponseEntity<String> delete(HttpServletRequest request, @PathVariable Long postId) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(declarationService.delete(userId, postId));
    }


}
