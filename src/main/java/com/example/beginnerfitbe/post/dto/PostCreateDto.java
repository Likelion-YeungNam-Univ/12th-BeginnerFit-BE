package com.example.beginnerfitbe.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PostCreateDto {
    private String title;
    private String content;
    private String categoryName;

    @Builder
    public PostCreateDto(String title, String content, String categoryName) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }

}
