package com.example.beginnerfitbe.playlist.dto;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PlaylistDto {
    private Long id;
    private String title;
    private String totalTime;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private Long userId;
    private List<YoutubeVideoDto> videos;

    @Builder
    public PlaylistDto(Long id, String title, String totalTime, Boolean isCompleted, LocalDateTime createdAt, Long userId, List<YoutubeVideoDto> videos) {
        this.id = id;
        this.title = title;
        this.totalTime = totalTime;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.userId = userId;
        this.videos = videos;
    }

    public static PlaylistDto fromEntity(Playlist playlist) {
        return PlaylistDto.builder()
                .id(playlist.getId())
                .title(playlist.getTitle())
                .totalTime(playlist.getTotalTime())
                .isCompleted(playlist.getIsCompleted())
                .createdAt(playlist.getCreatedAt())
                .userId(playlist.getUser().getId())
                .videos(playlist.getVideos().stream()
                        .map(YoutubeVideoDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
