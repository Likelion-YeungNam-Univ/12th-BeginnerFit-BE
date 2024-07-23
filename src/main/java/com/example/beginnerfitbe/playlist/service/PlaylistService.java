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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeUtil youtubeUtil;

    public PlaylistDto create(User user) throws IOException {
        String query = searchKeyword(user);
        String requestTime = String.valueOf(user.getExerciseTime());
        SelectedVideoDto selectVideoDto = youtubeUtil.selectVideos(query, requestTime);

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
        System.out.println("플레이리스트 생성");
        return PlaylistDto.fromEntity(playlist);
    }

    private String searchKeyword(User user) {

        String query= null;
        if(user.getExerciseIntensity()<=3){
            query = user.getExercisePart() + " " + user.getExercisePurpose() +" 쉬운 운동";
        }
        else if(user.getExerciseIntensity()>=7){
            query = user.getExercisePart() + " " + user.getExercisePurpose() +" 매운맛 운동";
        }
        else{
            query = user.getExercisePart() + " " + user.getExercisePurpose()+" 운동";
        }
        return query;
    }

    @Transactional
    public void update(Long playlistId) {
        List<YoutubeVideoDto> samePlaylistVideos = youtubeVideoService.getYoutubeVideosByPlaylist(playlistId); // 비디오 상태 확인

        boolean allWatched = samePlaylistVideos.stream().allMatch(YoutubeVideoDto::getIsWatched);

        if (allWatched) {
            Playlist playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));
            playlist.setCompleted(true);
            playlistRepository.save(playlist);
        }
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

    //사용자 플레이리스트 목록
    public List<PlaylistDto> me(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return playlistRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(PlaylistDto::fromEntity)
                .collect(Collectors.toList());
    }

    //가장 최신 플레이리스트 (홈화면에 뜰 플레이리스트 )
    public PlaylistDto getRecentPlaylist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Playlist playlist = playlistRepository.findFirstByUserOrderByCreatedAtDesc(user).orElseThrow(() -> new IllegalArgumentException("Playlist not found"));
        return PlaylistDto.fromEntity(playlist);
    }

}