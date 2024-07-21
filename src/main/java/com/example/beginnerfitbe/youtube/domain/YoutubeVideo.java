package com.example.beginnerfitbe.youtube.domain;

import com.example.beginnerfitbe.playlist.domain.Playlist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class YoutubeVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String videoId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String thumbnail;

    @Column
    private String description;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = false)
    private String publishedAt;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private Boolean isWatched;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @Builder
    public YoutubeVideo(String title, String videoId, String url, String thumbnail, String description, String channel, String publishedAt, String duration, Boolean isWatched, Playlist playlist) {
        this.title = title;
        this.videoId = videoId;
        this.url = url;
        this.thumbnail = thumbnail;
        this.description = description;
        this.channel = channel;
        this.publishedAt = publishedAt;
        this.duration = duration;
        this.isWatched = isWatched;
        this.playlist = playlist;
    }
}
