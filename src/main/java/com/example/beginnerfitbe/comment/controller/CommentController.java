package com.example.beginnerfitbe.comment.controller;

import com.example.beginnerfitbe.comment.dto.CommentCreateDto;
import com.example.beginnerfitbe.comment.dto.CommentUpdateDto;
import com.example.beginnerfitbe.comment.service.CommentService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "댓글 생성 메서드", description = "게시글번호/내용을 입력받아 댓글을 생성합니다.")
    private ResponseEntity<?> create(HttpServletRequest request, @RequestBody CommentCreateDto commentCreateDto){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return commentService.create(userId, commentCreateDto);
    }

    @GetMapping("")
    @Operation(summary = "댓글 조회 메서드", description = "전체 댓글을 조회합니다.")
    private ResponseEntity<?> list() {
        return ResponseEntity.ok(commentService.list());
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "댓글 상세 조회 메서드", description = "댓글 상세 정보를 조회합니다.")
    private ResponseEntity<?> read(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.read(commentId));
    }

    @GetMapping("/me")
    @Operation(summary = "사용자 댓글 조회 메소드", description = "사용자가 마이페이지에서 자신이 등록한 댓글을 조회합니다.")
    private ResponseEntity<?> me(HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(commentService.me(userId));
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "게시글 댓글 조회 메소드", description = "게시글에 등록한 댓글을 조회합니다.")
    private ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PostMapping("/update/{commentId}")
    @Operation(summary = "댓글 수정 메서드", description = "댓글번호/내용을 입력받아 댓글을 수정합니다.")
    private ResponseEntity<?> update(HttpServletRequest request, @PathVariable Long commentId, @RequestBody CommentUpdateDto commentUpdateDto){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return commentService.update(userId, commentId, commentUpdateDto);
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(summary = "댓글 삭제 메서드", description = "사용자가 작성한 댓글을 삭제합니다.")
    private ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long commentId){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return commentService.delete(userId, commentId);
    }

}
