package com.example.beginnerfitbe.like.dto;

import com.example.beginnerfitbe.like.domain.PostLike;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostLikeDto {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;

    public PostLikeDto(Long id, Long userId, Long postId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static PostLikeDto fromEntity(PostLike postLike){
        return new PostLikeDto(
                postLike.getId(),
                postLike.getUser().getId(),
                postLike.getPost().getId(),
                postLike.getCreatedAt());

    }
}
