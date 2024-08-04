package com.example.beginnerfitbe.comment.dto;

import com.example.beginnerfitbe.comment.domain.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public CommentDto(Long id, Long postId, Long userId, String username, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getContent(),
                comment.getCreatedAt()
        );

    }
}
