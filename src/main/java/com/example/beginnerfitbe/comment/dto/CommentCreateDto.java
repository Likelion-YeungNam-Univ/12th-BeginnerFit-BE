package com.example.beginnerfitbe.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateDto {
    private Long postId;
    private String content;
    @Builder
    public CommentCreateDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
