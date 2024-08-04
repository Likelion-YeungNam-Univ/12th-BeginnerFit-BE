package com.example.beginnerfitbe.playlist.repository;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import com.example.beginnerfitbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserOrderByCreatedAtDesc(User user);
    Optional<Playlist> findFirstByUserOrderByCreatedAtDesc(User user);

    List<Playlist> findByUserId(Long userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Playlist p WHERE p.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
