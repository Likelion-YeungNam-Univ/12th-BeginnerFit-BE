package com.example.beginnerfitbe.playlist.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserOrderByCreatedAtDesc(User user);
    Optional<Playlist> findFirstByUserOrderByCreatedAtDesc(User user);
}
