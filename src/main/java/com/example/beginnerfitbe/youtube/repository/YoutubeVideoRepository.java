package com.example.beginnerfitbe.youtube.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findVidoesByPlaylist(Playlist playlist);
    List<YoutubeVideo> findByPlaylist_UserId(Long userId);
    Optional<YoutubeVideo> findFirstByPlaylist_UserIdOrderByWatchedTimeDesc(Long userId);
}