package com.example.beginnerfitbe.playlist.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.playlist.dto.PlaylistDto;
import com.example.beginnerfitbe.playlist.repository.PlaylistRepository;
import com.example.beginnerfitbe.youtube.dto.SelectedVideoDto;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.service.YoutubeVideoService;
import com.example.beginnerfitbe.youtube.util.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeUtil youtubeUtil;

    public PlaylistDto create(String query, String duration, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        SelectedVideoDto selectVideoDto = youtubeUtil.selectVideos(query, duration);

        Playlist playlist = Playlist.builder()
                .title(query + " 집중 공략하기 플리")
                .createdAt(LocalDateTime.now())
                .isCompleted(false)
                .totalTime(selectVideoDto.getTotalTime())
                .user(user)
                .videos(selectVideoDto.getYoutubeVideos())
                .build();

        playlist = playlistRepository.save(playlist);

        youtubeVideoService.create(selectVideoDto, playlist);
        return PlaylistDto.fromEntity(playlist);
    }

    //재생목록 조회
    public List<PlaylistDto> list(){
        return playlistRepository.findAll().stream()
                .map(PlaylistDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PlaylistDto read(Long playlistId){
        Playlist playlist =  playlistRepository.findById(playlistId).orElseThrow(() -> new IllegalArgumentException("Playlist not found"));
        return PlaylistDto.fromEntity(playlist);
    }

    public List<PlaylistDto> getPlaylistsByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return playlistRepository.findPlaylistByUser(user).stream()
                .map(PlaylistDto::fromEntity)
                .collect(Collectors.toList());
    }

}