package com.example.beginnerfitbe.scrap.controller;


import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/scraps")
    @Operation(summary = "스크랩 전체 조회 메소드", description = "전체 스크랩 목록을 조회합니다.")
    private ResponseEntity<?> list(){
        return ResponseEntity.ok(scrapService.list());
    }

    @GetMapping("/scraps/me")
    @Operation(summary = "내가 스크랩 한 내역 조회 메소드", description = "내가 스크랩한 게시글을 조회합니다.")
    private ResponseEntity<?> getScrapsByUser(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(scrapService.getScrapsByUser(userId));
    }

    @GetMapping("/{postId}/scraps")
    @Operation(summary = "게시글 별 스크랩 조회 메소드", description = "게시글의 스크랩 내역을 조회합니다.")
    private ResponseEntity<?> getScrapsByPost(@PathVariable Long postId){
        return ResponseEntity.ok(scrapService.getScrapsByPost(postId));
    }

    @GetMapping("/{postId}/scraps/check")
    @Operation(summary = "스크랩 여부 조회 메소드", description = "게시글을 스크랩 했는 지 조회합니다.")
    private ResponseEntity<?> checkScrap(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(scrapService.checkScrap(userId, postId));
    }

}
