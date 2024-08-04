package com.example.beginnerfitbe.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreateDto {
    private String content;
    @Builder
    public CommentCreateDto(String content) {
        this.content = content;
    }
}
