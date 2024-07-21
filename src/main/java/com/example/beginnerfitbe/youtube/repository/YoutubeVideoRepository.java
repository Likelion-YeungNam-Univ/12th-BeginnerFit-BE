package com.example.beginnerfitbe.youtube.repository;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo,Long> {
}
