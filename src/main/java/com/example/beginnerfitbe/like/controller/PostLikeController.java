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
@RequestMapping("/posts")
public class PostLikeController {
    private final JwtUtil jwtUtil;
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/likes")
    @Operation(summary = "게시글 좋아요 메서드", description = "게시글에 좋아요를 생성합니다.")
    public ResponseEntity<?> addLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.create(userId, postId));
    }

    @DeleteMapping("/{postId}/likes")
    @Operation(summary = "게시글 좋아요 취소 메서드", description = "게시글에 생성한 좋아요를 취소합니다.")
    public ResponseEntity<?> deleteLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.delete(userId, postId));
    }

    @GetMapping("/likes/me")
    @Operation(summary = "사용자 좋아요 조회 메소드", description = "사용자가 좋아요 한 내역을 조회합니다..")
    public ResponseEntity<?> getLikesByUser(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.getLikesByUser(userId));
    }

    @GetMapping("/{postId}/likes")
    @Operation(summary = "게시글 별 좋아요 조회 메소드", description = "게시글의 좋아요 내역을 조회합니다.")
    public ResponseEntity<?> getLikesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postLikeService.getLikesByPost(postId));
    }
}
