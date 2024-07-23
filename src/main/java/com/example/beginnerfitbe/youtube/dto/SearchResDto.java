package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SearchResDto {
    private String videoId;
    private String title;
    private String url;
    private String description;
    private String thumbnail;
    private String channel;
    private String publishedAt;
    private String duration;
    private Boolean isWatched;
    private LocalDateTime watchedTime;

    @Builder
    public SearchResDto(String videoId, String title, String url, String description, String thumbnail, String channel, String publishedAt, String duration, Boolean isWatched ,LocalDateTime watchedTime) {
        this.videoId = videoId;
        this.title = title;
        this.url = url;
        this.description = description;
        this.thumbnail = thumbnail;
        this.channel = channel;
        this.publishedAt = publishedAt;
        this.duration = duration;
        this. isWatched = isWatched;
        this. watchedTime =watchedTime;
    }

    public YoutubeVideo toEntity(){
        return YoutubeVideo.builder()
                .videoId(videoId)
                .title(title)
                .url(url)
                .description(description)
                .thumbnail(thumbnail)
                .channel(channel)
                .publishedAt(publishedAt)
                .duration(duration)
                .isWatched(false)
                .watchedTime(null)
                .build();
    }
}
