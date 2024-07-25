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
    private String profileUrl;
    private String categoryName;

    public PostDto(Long id, String title, String content, String pictureUrl, LocalDateTime createdAt, Long userId, String userName, String profileUrl, String categoryName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pictureUrl=pictureUrl;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.categoryName = categoryName;
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
                post.getUser().getProfileUrl(),
                post.getCategory().getCategoryName()
        );
    }
}
