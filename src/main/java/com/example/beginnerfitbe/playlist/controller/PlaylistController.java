package com.example.beginnerfitbe.playlist.controller;

import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.playlist.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("")
    @Operation(summary = "플레이리스 생성 메소드", description = "사용자가 입력한 키워드를 기반으로 플레이리스트를 생성합니다.")
    public ResponseEntity<?> createYoutubeVideos(HttpServletRequest request,  @RequestParam(value = "keyword") String keyword, @RequestParam (value = "duration")String duration) throws IOException, IOException {
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(playlistService.create(keyword, duration, userId));
    }

    @GetMapping("")
    @Operation(summary = "플레이리스 전체 조회 메소드", description = "전체 플레이리스트를 조회합니다.")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(playlistService.list());
    }

    @GetMapping("/{playlistId}")
    @Operation(summary = "플레이리스 상세 조회 메소드", description = "플레이리스트 상세 정보를 조회합니다.")
    public ResponseEntity<?> read(@PathVariable Long playlistId){
        return ResponseEntity.ok(playlistService.read(playlistId));
    }

    @GetMapping("/me")
    @Operation(summary = "사용자의 플레이리스트 목록을 조회합니다.", description = "사용자의 플레이리스트 목록을 조회합니다.")
    public ResponseEntity<?> getPlaylistsByUser(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.resolveToken(request).substring(7));
        return ResponseEntity.ok(playlistService.getPlaylistsByUser(userId));
    }
}
