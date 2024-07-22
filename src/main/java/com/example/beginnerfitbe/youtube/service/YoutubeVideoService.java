package com.example.beginnerfitbe.youtube.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.playlist.repository.PlaylistRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import com.example.beginnerfitbe.youtube.dto.SelectedVideoDto;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.repository.YoutubeVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class YoutubeVideoService {

    private final YoutubeVideoRepository youtubeVideoRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;


    public void create(SelectedVideoDto playlistDto, Playlist playlist){
        List<YoutubeVideo> youtubeVideos = playlistDto.getYoutubeVideos();
        youtubeVideos.forEach(video -> video.updatePlaylist(playlist));
        youtubeVideoRepository.saveAll(youtubeVideos);
    }

    @Transactional
    public YoutubeVideoDto watchVideo(Long userId ,Long videoId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        YoutubeVideo youtubeVideo = youtubeVideoRepository.findById(videoId).orElseThrow(() -> new IllegalArgumentException("Video not found"));
        if(!user.getId().equals( youtubeVideo.getPlaylist().getUser().getId()))  throw new IllegalArgumentException("재생목록 주인만 시청 상태 변경 가능.");

        //시청 true
        youtubeVideo.watched(true);
        youtubeVideoRepository.save(youtubeVideo);

        return YoutubeVideoDto.fromEntity(youtubeVideo);
    }

    //비디오 조회
    public List<YoutubeVideoDto> list(){
        return youtubeVideoRepository.findAll().stream()
                .map(YoutubeVideoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public YoutubeVideoDto read(Long videoId){
        YoutubeVideo youtubeVideo = youtubeVideoRepository.findById(videoId).orElseThrow(() -> new IllegalArgumentException("Video not found"));

        return YoutubeVideoDto.fromEntity(youtubeVideo);
    }

    public List<YoutubeVideoDto> getYoutubeVideosByPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new IllegalArgumentException("Playlist not found"));
        return youtubeVideoRepository.findVidoesByPlaylist(playlist).stream()
                .map(YoutubeVideoDto::fromEntity)
                .collect(Collectors.toList());
    }

}
