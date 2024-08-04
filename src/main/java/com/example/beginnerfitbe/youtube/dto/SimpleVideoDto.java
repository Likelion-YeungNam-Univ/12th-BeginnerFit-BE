package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleVideoDto {
    private Long id;
    private String videoYoutubeId;

    @Builder
    public SimpleVideoDto(Long id, String videoYoutubeId) {
        this.id = id;
        this.videoYoutubeId = videoYoutubeId;
    }

    public static SimpleVideoDto fromEntity(YoutubeVideo video) {
        return SimpleVideoDto.builder()
                .id(video.getId())
                .videoYoutubeId(video.getVideoId())
                .build();
    }
}
