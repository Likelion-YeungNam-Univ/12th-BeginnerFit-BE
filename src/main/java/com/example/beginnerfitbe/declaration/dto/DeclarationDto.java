package com.example.beginnerfitbe.declaration.dto;

import com.example.beginnerfitbe.declaration.domain.Declaration;
import lombok.Data;

@Data
public class DeclarationDto {
    private Long userId;
    private String userName;
    private Long postId;
    private String postTitle;
    private String reason;

    public DeclarationDto(Long userId, String userName, Long postId, String postTitle, String reason) {
        this.userId = userId;
        this.userName = userName;
        this.postId = postId;
        this.postTitle = postTitle;
        this.reason = reason;
    }

    public static DeclarationDto fromEntity(Declaration declaration) {
        return new DeclarationDto(
                declaration.getUser().getId(),
                declaration.getUser().getName(),
                declaration.getPost().getId(),
                declaration.getPost().getTitle(),
                declaration.getReason().toString()
        );
    }

}
