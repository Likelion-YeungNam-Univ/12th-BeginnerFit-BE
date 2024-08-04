package com.example.beginnerfitbe.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateDto {
    private String title;
    private String content;
    private String categoryName;
}
