package com.example.beginnerfitbe.playlist.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.playlist.repository.PlaylistRepository;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.youtube.service.YoutubeVideoService;
import com.example.beginnerfitbe.youtube.util.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeUtil youtubeUtil;

    public void create(String query, String duration, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        YoutubeVideoDto youtubeVideoDto = youtubeUtil.selectVideos(query, duration);

        // 1. Playlist 저장
        Playlist playlist = Playlist.builder()
                .title(user.getName() + "님을(를) 위한 플레이리스트")
                .description(query + " 키워드 기반 추천 영상")
                .createdAt(LocalDateTime.now())
                .isCompleted(false)
                .totalTime(youtubeVideoDto.getTotalTime())
                .user(user)
                .videos(youtubeVideoDto.getYoutubeVideos())
                .build();

        playlist = playlistRepository.save(playlist);

        // 2. YoutubeVideo 저장
        youtubeVideoService.create(youtubeVideoDto, playlist);
    }

}