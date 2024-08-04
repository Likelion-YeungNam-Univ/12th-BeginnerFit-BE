package com.example.beginnerfitbe.scrap.dto;

import com.example.beginnerfitbe.post.dto.PostDto;
import com.example.beginnerfitbe.scrap.domain.Scrap;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScrapDto {
    private Long userId;
    private PostDto postInfo;
    private LocalDateTime createdAt;

    public ScrapDto(Long userId, PostDto postInfo, LocalDateTime createdAt) {
        this.userId = userId;
        this.postInfo = postInfo;
        this.createdAt = createdAt;
    }
    public static ScrapDto fromEntity(Scrap scrap){
        return new ScrapDto(
                scrap.getUser().getId(),
                PostDto.fromEntity(scrap.getPost()),
                scrap.getCreatedAt()
        );
    }
}
