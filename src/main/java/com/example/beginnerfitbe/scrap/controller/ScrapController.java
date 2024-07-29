package com.example.beginnerfitbe.scrap.controller;


import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class ScrapController {

    private final JwtUtil jwtUtil;
    private final ScrapService scrapService;

    @PostMapping("/{postId}/scraps")
    @Operation(summary = "게시글 스크랩 메서드", description = "게시글을 스크랩합니다.")
    private ResponseEntity<?> create(HttpServletRequest request , @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(scrapService.create(userId, postId));
    }
}
