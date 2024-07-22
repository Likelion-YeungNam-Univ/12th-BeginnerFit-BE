package com.example.beginnerfitbe.youtube.controller;

import com.example.beginnerfitbe.youtube.service.YoutubeVideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class YoutubeVideoController {

    private final YoutubeVideoService youtubeVideoService;

    //비디오 선택 ->  시청 true
    @PostMapping("/videos/{videoId}")
    @Operation(summary = "비디오 선택 메소드", description = "비디오를 선택하면 시청 한 상태로 업데이트 합니다.")
    private ResponseEntity<?> watchVideo(@PathVariable Long videoId){
        return ResponseEntity.ok(youtubeVideoService.watchVideo(videoId));
    }

    @GetMapping("/videos")
    @Operation(summary = "비디오 전체 조회 메소드", description = "전체 비디오를 조회합니다.")
    private ResponseEntity<?> list(){
        return ResponseEntity.ok(youtubeVideoService.list());
    }

    @GetMapping("/videos/{videoId}")
    @Operation(summary = "비디오 상세 조회 메소드", description = "비디오의 상세 정보를 조회합니다.")
    private ResponseEntity<?> read(@PathVariable Long videoId){
        return ResponseEntity.ok(youtubeVideoService.read(videoId));
    }


    @GetMapping("/{playlistId}/videos")
    @Operation(summary = "플레이리스트 별 비디오 조회", description = "플레이리스트에 해당하는 비디오를 조회합니다.")
    private ResponseEntity<?> getYoutubeVideosByPlaylist(@PathVariable Long playlistId){
        return ResponseEntity.ok(youtubeVideoService.getYoutubeVideosByPlaylist(playlistId));
    }

}
