package com.example.beginnerfitbe.playlist.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
