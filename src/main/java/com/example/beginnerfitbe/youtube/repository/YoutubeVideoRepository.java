package com.example.beginnerfitbe.youtube.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findVideosByPlaylist(Playlist playlist);
    List<YoutubeVideo> findByPlaylist_UserId(Long userId);
    YoutubeVideo findFirstByPlaylist_UserIdOrderByWatchedTimeDesc(Long userId);

    List<YoutubeVideo> findByPlaylist_UserIdAndIsWatchedOrderByWatchedTimeDesc(Long userId, boolean isWatched);

    List<YoutubeVideo> findTop3ByPlaylist_UserIdAndIsWatchedOrderByWatchedTimeDesc(Long userId, boolean isWatched);

    @Transactional
    @Modifying
    @Query("DELETE FROM YoutubeVideo y WHERE y.playlist.id = :playlistId")
    void deleteByPlaylistId(Long playlistId);

}