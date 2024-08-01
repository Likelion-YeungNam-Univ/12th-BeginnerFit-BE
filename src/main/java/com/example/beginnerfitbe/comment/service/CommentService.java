package com.example.beginnerfitbe.comment.service;

import com.example.beginnerfitbe.alarm.service.AlarmService;
import com.example.beginnerfitbe.comment.domain.Comment;
import com.example.beginnerfitbe.comment.dto.CommentCreateDto;
import com.example.beginnerfitbe.comment.dto.CommentDto;
import com.example.beginnerfitbe.comment.dto.CommentUpdateDto;
import com.example.beginnerfitbe.comment.repository.CommentRepository;
import com.example.beginnerfitbe.error.StateResponse;
import com.example.beginnerfitbe.post.domain.Post;
import com.example.beginnerfitbe.post.repository.PostRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AlarmService alarmService;

    public ResponseEntity<StateResponse> create(Long userId, Long postId, CommentCreateDto commentCreateDto){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));

        Comment comment = Comment.builder()
                .content(commentCreateDto.getContent())
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        String alarmMessage = user.getName() + " 님이 댓글을 달았습니다.";
        alarmService.createAlarm(post.getUser(), alarmMessage); // 알림 생성

        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 생성했습니다.").build());
    }

    public List<CommentDto> list(){
        return commentRepository.findAll().stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CommentDto read(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found comment"));
        return CommentDto.fromEntity(comment);
    }

    public List<CommentDto> getCommentsByPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("not found post"));
        return commentRepository.findCommentsByPost(post).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentDto> me (Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
        return commentRepository.findCommentsByUser(user).stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ResponseEntity<StateResponse> update(Long id, Long commentId, CommentUpdateDto commentUpdateDto){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found comment"));
        Long userId = comment.getUser().getId();

        if(!userId.equals(id)) throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        String content= commentUpdateDto.getContent();

        if(content!=null){
            comment.update(commentUpdateDto.getContent());
        }
        commentRepository.save(comment);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 수정했습니다.").build());
    }

    @Transactional
    public ResponseEntity<StateResponse> delete(Long id, Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found comment"));
        Long userId = comment.getUser().getId();

        if(!userId.equals(id)) throw new IllegalArgumentException("작성자만 댓글을 삭제할 수 있습니다.");

        commentRepository.delete(comment);
        return ResponseEntity.ok(StateResponse.builder().code("SUCCESS").message("댓글을 성공적으로 삭제했습니다.").build());
    }

}
