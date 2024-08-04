package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class YoutubeVideoDto {
    private Long id;
    private String videoId;
    private String title;
    private String url;
    private String thumbnail;
    private String description;
    private String channel;
    private String publishedAt;
    private String duration;
    private Boolean isWatched;
    private LocalDateTime watchedTime;
    private Long playlistId;

    @Builder
    public YoutubeVideoDto(Long id, String videoId, String title, String url, String thumbnail, String description, String channel, String publishedAt, String duration, Boolean isWatched, LocalDateTime watchedTime, Long playlistId) {
        this.id = id;
        this.videoId = videoId;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.description = description;
        this.channel = channel;
        this.publishedAt = publishedAt;
        this.duration = duration;
        this.isWatched = isWatched;
        this.watchedTime = watchedTime;
        this.playlistId = playlistId;
    }

    public static YoutubeVideoDto fromEntity(YoutubeVideo youtubeVideo) {
        return YoutubeVideoDto.builder()
                .id(youtubeVideo.getId())
                .videoId(youtubeVideo.getVideoId())
                .title(youtubeVideo.getTitle())
                .url(youtubeVideo.getUrl())
                .thumbnail(youtubeVideo.getThumbnail())
                .description(youtubeVideo.getDescription())
                .channel(youtubeVideo.getChannel())
                .publishedAt(youtubeVideo.getPublishedAt())
                .duration(youtubeVideo.getDuration())
                .isWatched(youtubeVideo.getIsWatched())
                .watchedTime(youtubeVideo.getWatchedTime())
                .playlistId(youtubeVideo.getPlaylist().getId())
                .build();
    }
}
