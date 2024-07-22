package com.example.beginnerfitbe.youtube.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.user.domain.User;
import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findVidoesByPlaylist(Playlist playlist);
}