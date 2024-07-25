package com.example.beginnerfitbe.youtube.service;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.playlist.repository.PlaylistRepository;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.user.repository.UserRepository;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import com.example.beginnerfitbe.youtube.dto.RecentVideoDto;
import com.example.beginnerfitbe.youtube.dto.SelectedVideoDto;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.repository.YoutubeVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        return youtubeVideoRepository.findVideosByPlaylist(playlist).stream()
                .map(YoutubeVideoDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자 비디오 목록 조회
    public List<YoutubeVideoDto> getWatchedVideo(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return youtubeVideoRepository.findByPlaylist_UserIdAndIsWatchedOrderByWatchedTimeDesc(userId, true).stream()
                .map(YoutubeVideoDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<YoutubeVideoDto> getRecentWatchedVideo(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return youtubeVideoRepository.findTop3ByPlaylist_UserIdAndIsWatchedOrderByWatchedTimeDesc(userId, true).stream()
                .map(YoutubeVideoDto::fromEntity)
                .collect(Collectors.toList());
    }



    // 다음 영상
    public RecentVideoDto getNextVideo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: "));

        // 가장 최근 플레이리스트 조회
        Playlist recentPlaylist = playlistRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new IllegalArgumentException("Recent playlist not found"));

        List<YoutubeVideo> videos = youtubeVideoRepository.findVideosByPlaylist(recentPlaylist);
        if (videos.isEmpty()) {
            throw new IllegalArgumentException("video not found");
        }

        // 최근 시청 비디오 조회
        YoutubeVideo lastWatchedVideo = youtubeVideoRepository.findFirstByPlaylist_UserIdOrderByWatchedTimeDesc(userId);

        // 최근 시청 비디오가 없는 경우 첫 번째 비디오 반환
        if (!lastWatchedVideo.getIsWatched()) {
            System.out.println("아직 아무것도 안봄");
            return RecentVideoDto.fromEntity(videos.get(0));
        }

        int lastWatchedIndex = videos.indexOf(lastWatchedVideo);

        if (lastWatchedIndex == videos.size()-1) {
            System.out.println(" 다 봄");
            return null;
        }

        YoutubeVideo nextVideo = videos.get(lastWatchedIndex+1);
        return RecentVideoDto.fromEntity(nextVideo);
    }


}
