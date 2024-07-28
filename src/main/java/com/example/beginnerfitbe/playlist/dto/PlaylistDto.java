package com.example.beginnerfitbe.playlist.dto;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.dto.SimpleVideoDto;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    private List<TagDto> tags;
    private List<SimpleVideoDto> videos;


    @Builder
    public PlaylistDto(Long id, String title, String totalTime, Boolean isCompleted, LocalDateTime createdAt, Long userId, List<SimpleVideoDto> videos, List<TagDto> tags) {
        this.id = id;
        this.title = title;
        this.totalTime = totalTime;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.userId = userId;
        this.videos = videos;
        this.tags = tags;
    }

    public static PlaylistDto fromEntity(Playlist playlist) {
        Set<String> excludedWords = Set.of("운동", "집중공략", "플리");
        String titleWithoutExcludedWords = Arrays.stream(playlist.getTitle().split(" "))
                .filter(word -> !excludedWords.contains(word))
                .collect(Collectors.joining(" "));

        List<TagDto> tags = Arrays.stream(titleWithoutExcludedWords.split(" "))
                .map(TagDto::new)
                .collect(Collectors.toList());

        return PlaylistDto.builder()
                .id(playlist.getId())
                .title(playlist.getTitle())
                .totalTime(playlist.getTotalTime())
                .isCompleted(playlist.getIsCompleted())
                .createdAt(playlist.getCreatedAt())
                .userId(playlist.getUser().getId())
                .videos(playlist.getVideos().stream()
                        .map(SimpleVideoDto::fromEntity)
                        .collect(Collectors.toList()))
                .tags(tags)
                .build();
    }
}
