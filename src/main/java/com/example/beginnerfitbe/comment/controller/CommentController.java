package com.example.beginnerfitbe.comment.controller;

import com.example.beginnerfitbe.comment.dto.CommentCreateDto;
import com.example.beginnerfitbe.comment.service.CommentService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    @PostMapping("/create")
    private ResponseEntity<?> create(HttpServletRequest request, @RequestBody CommentCreateDto commentCreateDto){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return commentService.create(userId, commentCreateDto);
    }

}
