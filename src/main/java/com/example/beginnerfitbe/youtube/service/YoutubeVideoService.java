package com.example.beginnerfitbe.youtube.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.repository.YoutubeVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class YoutubeVideoService {

    private final YoutubeVideoRepository youtubeVideoRepository;

    public void create(YoutubeVideoDto youtubeVideoDto, Playlist playlist) throws IOException {
        List<YoutubeVideo> youtubeVideos = youtubeVideoDto.getYoutubeVideos();
        youtubeVideos.forEach(video -> video.setPlaylist(playlist));
        youtubeVideoRepository.saveAll(youtubeVideos);
    }


}
