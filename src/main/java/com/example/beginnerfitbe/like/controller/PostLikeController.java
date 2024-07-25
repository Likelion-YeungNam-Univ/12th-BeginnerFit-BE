package com.example.beginnerfitbe.like.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.like.service.PostLikeService;
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
    public ResponseEntity<?> addLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.create(userId, postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteLike(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postLikeService.delete(userId, postId));
    }
}
