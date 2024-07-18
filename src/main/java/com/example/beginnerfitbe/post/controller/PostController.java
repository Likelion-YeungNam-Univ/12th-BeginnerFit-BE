package com.example.beginnerfitbe.post.controller;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.post.dto.PostCreateDto;
import com.example.beginnerfitbe.post.dto.PostUpdateDto;
import com.example.beginnerfitbe.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    @Operation(summary = "게시글 조회 메서드", description = "전체 게시글을 조회합니다.")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(postService.list());
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회 메서드", description = "게시글 상세 정보를 조회합니다.")
    public ResponseEntity<?> read(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.read(postId));
    }
    @GetMapping("/me")
    @Operation(summary = "사용자 게시글 조회 메소드", description = "사용자가 마이페이지에서 자신이 등록한 글을 조회합니다.")
    public ResponseEntity<?> me(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(postService.me(userId));
    }

    @GetMapping("/category/{categoryName}")
    @Operation(summary = "카테고리 별 게시글 조회 메소드", description = "카테고리 이름을 통해 카테고리 별로 게시글을 조회합니다.")
    public ResponseEntity<?> getPostsByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(postService.getPostsByCategoryName(categoryName));
    }

    @PostMapping("/create")
    @Operation(summary = "게시글 생성 메서드", description = "제목/내용/카테고리이름을 입력받아 게시글을 생성합니다.")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody PostCreateDto createDto) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));

        return postService.create(userId,createDto);
    }
    @PostMapping("/update/{postId}")
    @Operation(summary = "사용자 글 수정 메서드", description = "사용자가 마이페이지에서 자신이 작성한 글을 수정합니다.")
    ResponseEntity<StateResponse> update(HttpServletRequest request, @PathVariable Long postId, @RequestBody PostUpdateDto postUpdateDto){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return postService.update(postId, userId,postUpdateDto);
    }
    @DeleteMapping("/delete/{postId}")
    @Operation(summary = "게시글 삭제 메서드", description = "사용자가 커뮤니티 글을 삭제하기 위한 메서드입니다.")
    ResponseEntity<StateResponse> delete(HttpServletRequest request, @PathVariable Long postId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return postService.delete(postId, userId);
    }


}
