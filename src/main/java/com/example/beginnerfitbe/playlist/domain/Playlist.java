package com.example.beginnerfitbe.playlist.domain;

import com.example.beginnerfitbe.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private int totalTime;

    @Column(nullable = false)
    private boolean isCompleted;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Playlist(String title, String description, int totalTime, boolean isCompleted, LocalDateTime createdAt, User user) {
        this.title = title;
        this.description = description;
        this.totalTime = totalTime;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.user = user;
    }
}
