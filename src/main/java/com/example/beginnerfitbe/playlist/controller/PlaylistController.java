package com.example.beginnerfitbe.playlist.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.playlist.service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final JwtUtil jwtUtil;
    private final PlaylistService playlistService;

    @PostMapping("/create")
    public ResponseEntity<?> createYoutubeVideos(HttpServletRequest request,  @RequestParam(value = "keyword") String query, @RequestParam (value = "duration")String duration) throws IOException {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        playlistService.create(query, duration, userId);
        return ResponseEntity.ok("Youtube videos created successfully");
    }
}
