package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Data;

@Data
public class RecentVideoDto {
    private Long id;
    private String videoId;
    private String url;
    private String title;

    @Builder
    public RecentVideoDto(Long id, String videoId, String url, String title) {
        this.id = id;
        this.videoId = videoId;
        this.url = url;
        this.title = title;
    }

    public static RecentVideoDto fromEntity(YoutubeVideo youtubeVideo) {
        return RecentVideoDto.builder()
                .id(youtubeVideo.getId())
                .videoId(youtubeVideo.getVideoId())
                .url(youtubeVideo.getUrl())
                .title(youtubeVideo.getTitle())
                .build();
    }
}
