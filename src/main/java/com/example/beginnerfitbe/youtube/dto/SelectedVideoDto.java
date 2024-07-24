package com.example.beginnerfitbe.youtube.dto;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import lombok.Data;

import java.util.List;

@Data
public class SelectedVideoDto {
    private List<YoutubeVideo> youtubeVideos;
    private String totalTime;
    private Playlist playlist;
}
