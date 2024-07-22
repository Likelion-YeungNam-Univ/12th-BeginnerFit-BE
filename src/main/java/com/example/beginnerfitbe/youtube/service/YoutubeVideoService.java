package com.example.beginnerfitbe.youtube.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import com.example.beginnerfitbe.youtube.dto.SelectedVideoDto;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.repository.YoutubeVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class YoutubeVideoService {

    private final YoutubeVideoRepository youtubeVideoRepository;

    public void create(SelectedVideoDto playlistDto, Playlist playlist){
        List<YoutubeVideo> youtubeVideos = playlistDto.getYoutubeVideos();
        youtubeVideos.forEach(video -> video.setPlaylist(playlist));
        youtubeVideoRepository.saveAll(youtubeVideos);
    }

    public YoutubeVideoDto watchVideo(Long videoId) {
        YoutubeVideo youtubeVideo = youtubeVideoRepository.findById(videoId).orElseThrow(() -> new IllegalArgumentException("Video not found"));

        //시청 true
        youtubeVideo.watched(true);
        youtubeVideoRepository.save(youtubeVideo);

        return YoutubeVideoDto.fromEntity(youtubeVideo);
    }
}
