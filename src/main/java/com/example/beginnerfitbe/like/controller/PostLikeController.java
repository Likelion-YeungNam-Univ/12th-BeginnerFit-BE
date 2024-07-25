package com.example.beginnerfitbe.like.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.like.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class PostLikeController {
    private final JwtUtil jwtUtil;
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    @Operation(summary = "게시글 좋아요 메서드", description = "게시글에 좋아요를 생성합니다.")
    public ResponseEntity<?> addLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.create(userId, postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 좋아요 취소 메서드", description = "게시글에 생성한 좋아요를 취소합니다.")
    public ResponseEntity<?> deleteLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.delete(userId, postId));
    }
}
