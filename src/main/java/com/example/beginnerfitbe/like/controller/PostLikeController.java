package com.example.beginnerfitbe.like.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.like.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
