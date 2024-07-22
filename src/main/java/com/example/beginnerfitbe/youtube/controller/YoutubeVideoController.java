package com.example.beginnerfitbe.youtube.controller;

import com.example.beginnerfitbe.youtube.service.YoutubeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class YoutubeVideoController {
    private final YoutubeVideoService youtubeVideoService;

    @PostMapping("/videos/{videoId}")
    private ResponseEntity<?> watchVideo(@PathVariable Long videoId){
        return ResponseEntity.ok(youtubeVideoService.watchVideo(videoId));
    }

}
