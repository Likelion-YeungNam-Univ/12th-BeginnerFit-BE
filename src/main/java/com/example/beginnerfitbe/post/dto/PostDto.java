package com.example.beginnerfitbe.post.dto;

import com.example.beginnerfitbe.post.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String pictureUrl;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;
    private String categoryName;
    private int likeCnt;
    private int declarationCnt;

    public PostDto(Long id, String title, String content, String pictureUrl, LocalDateTime createdAt, Long userId, String userName,  String categoryName, int likeCnt, int declarationCnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pictureUrl=pictureUrl;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
        this.categoryName = categoryName;
        this.declarationCnt =declarationCnt;
        this. likeCnt = likeCnt;
    }

    public static PostDto fromEntity(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPictureUrl(),
                post.getCreatedAt(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getCategory().getCategoryName(),
                post.getDeclarations().size(),
                post.getPostLikes().size()
        );
    }
}
