package com.example.beginnerfitbe.playlist.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.playlist.dto.PlaylistDto;
import com.example.beginnerfitbe.playlist.repository.PlaylistRepository;
import com.example.beginnerfitbe.user.dto.UserDto;
import com.example.beginnerfitbe.youtube.dto.SelectedVideoDto;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.service.YoutubeVideoService;
import com.example.beginnerfitbe.youtube.util.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);

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
        return PlaylistDto.fromEntity(playlist);
    }


    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void createPlaylistDaily() {
        userRepository.findAll().forEach(user -> {
            try {
                create(user);
            } catch (IOException e) {
                logger.error("플레이리스트 생성 중 오류 발생", e);
            }
        });
    }

    public String searchKeyword(User user) {
        List<String> keywords = new ArrayList<>();

        String areas = String.join(" ", user.getConcernedAreas()); // 고정된 부위
        List<String> goals = user.getExerciseGoals();
        List<String> intensities = user.getExerciseIntensity();

        // 목표와 강도를 조합하여 키워드 생성
        for (String goal : goals) {
            for (String intensity : intensities) {
                keywords.add(areas + " " + goal + " " + intensity + " 운동");
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(keywords.size());
        return keywords.get(randomIndex);
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