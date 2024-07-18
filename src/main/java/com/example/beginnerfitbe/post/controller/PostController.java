package com.example.beginnerfitbe.post.controller;

import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.post.dto.PostCreateDto;
import com.example.beginnerfitbe.post.service.PostService;
import com.example.beginnerfitbe.user.dto.SignUpReqDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "게시글 생성 메서드", description = "제목/내용/카테고리 내용을 입력받아 게시글을 생성합니다.")
    public ResponseEntity<StateResponse> update(HttpServletRequest request, @RequestBody PostCreateDto createDto) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));

        return postService.create(userId,createDto);
    }

}
