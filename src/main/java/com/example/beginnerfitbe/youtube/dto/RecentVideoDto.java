package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Data;

@Data
public class RecentVideoDto {
    private String url;
    private String title;

    @Builder
    public RecentVideoDto(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public static RecentVideoDto fromEntity(YoutubeVideo youtubeVideo) {
        return RecentVideoDto.builder()
                .url(youtubeVideo.getUrl())
                .title(youtubeVideo.getTitle())
                .build();
    }
}
