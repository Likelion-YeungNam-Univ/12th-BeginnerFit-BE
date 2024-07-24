package com.example.beginnerfitbe.playlist.dto;

import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PlaylistCreateDto {
    private String title;
    private String description;
    private int totalTime;
    private Boolean isWatched;
    private LocalDateTime createdAt;
    private User user;
    private List<YoutubeVideo> youtubeVideos;

    @Builder
    public PlaylistCreateDto(String title, String description, int totalTime, Boolean isWatched, LocalDateTime createdAt, User user, List<YoutubeVideo> youtubeVideos) {
        this.title = title;
        this.description = description;
        this.totalTime = totalTime;
        this.isWatched = isWatched;
        this.createdAt = createdAt;
        this.user = user;
        this.youtubeVideos = youtubeVideos;
    }
}
