package com.example.beginnerfitbe.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private Long userId;
    private String userName;
    private String categoryName;

}
