package com.example.beginnerfitbe.playlist.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
