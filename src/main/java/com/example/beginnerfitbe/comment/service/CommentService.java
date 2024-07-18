package com.example.beginnerfitbe.comment.service;

import com.example.beginnerfitbe.comment.domain.Comment;
import com.example.beginnerfitbe.comment.dto.CommentCreateDto;
import com.example.beginnerfitbe.comment.repository.CommentRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<StateResponse> create(Long userId, CommentCreateDto commentCreateDto){
        Post post = postRepository.findById(commentCreateDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("not found post"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));

        Comment comment = Comment.builder()
                .content(commentCreateDto.getContent())
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 생성했습니다.").build());
    }

}
