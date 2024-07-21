package com.example.beginnerfitbe.youtube.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class YoutubeSearchResDto {
    private String videoId;
    private String title;
    private String url;
    private String description;
    private String thumbnail;
    private String channel;
    private String publishedAt;
    private String duration;

    @Builder
    public YoutubeSearchResDto(String videoId, String title, String url, String description, String thumbnail, String channel, String publishedAt, String duration, Boolean isWatched) {
        this.videoId = videoId;
        this.title = title;
        this.url = url;
        this.description = description;
        this.thumbnail = thumbnail;
        this.channel = channel;
        this.publishedAt = publishedAt;
        this.duration = duration;
    }
}
