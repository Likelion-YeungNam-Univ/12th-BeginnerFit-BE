package com.example.beginnerfitbe.challengeparticipant.controller;

import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeDateDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeParticipantDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeRankingDto;
import com.example.beginnerfitbe.challengeparticipant.service.ChallengeParticipantService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/challengeparticipant")
@RequiredArgsConstructor
public class ChallengeParticipantController {

    private final ChallengeParticipantService challengeParticipantService;
    private final JwtUtil jwtUtil;

    @GetMapping("/completed-friend-count")
    @Operation(summary = "오늘챌린지 완료한 친구 수 확인 메서드", description = "오늘의 챌린지 완료한 친구 수를 확인합니다.")
    public long getCompletedFriendCount(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // "Bearer "를 제외한 부분

        List<OtherUserDto> acceptedRequestsDtos = challengeParticipantService.getAcceptedFriends(userId);
        List<Long> friendIds = acceptedRequestsDtos.stream()
                .map(OtherUserDto::getId)
                .collect(Collectors.toList());

        return challengeParticipantService.countCompletedFriends(friendIds);
    }

    @GetMapping("/today-challenges") // 현재 로그인한 유저의 챌린지 참가 정보 조회
    public ResponseEntity<List<ChallengeParticipantDTO>> getMyChallenges(HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // "Bearer "를 제외한 부분

        // 사용자 ID를 이용하여 챌린지 참가 정보 조회
        List<ChallengeParticipantDTO> challengeParticipants = challengeParticipantService.getUserChallenges(userId);

        // 조회된 챌린지 참가 정보를 응답으로 반환
        return ResponseEntity.ok(challengeParticipants);
    }

    @PutMapping("/complete/{challengeId}")
    @Operation(summary = "오늘챌린지 완료 처리 메서드", description = "오늘의 챌린지를 완료 처리합니다.")
    public ResponseEntity<Void> completeChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.completeChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @PutMapping("/notcomplete/{challengeId}")
    @Operation(summary = "오늘챌린지 실패 처리 메서드", description = "오늘의 챌린지를 실패 처리합니다.")
    public ResponseEntity<Void> notcompleteChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.notcompleteChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @PostMapping("/completed-month-challenges")
    @Operation(summary = "이번달 완료한 챌린지 리스트 조회 메서드", description = "이번달 완료한 챌린지 리스트를 조회 합니다.")
    public ResponseEntity<Map<String, Object>> getCompletedChallenges(HttpServletRequest request, @RequestBody ChallengeDateDTO challengeDateDTO) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeParticipantDTO> completedChallenges = challengeParticipantService.getCompletedChallengesByDate(
                userId, challengeDateDTO.getYear(), challengeDateDTO.getMonth());

        Map<String, Object> response = new HashMap<>();
        response.put("count", completedChallenges.size());
        response.put("challenges", completedChallenges);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    @Operation(summary = "오늘의 챌린지 순위를 조회 메서드", description = "오늘의 챌린지 순위를 조회 합니다.")
    public ResponseEntity<List<ChallengeRankingDto>> getChallengeRankings(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeRankingDto> rankings = challengeParticipantService.getChallengeRankings(userId);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/today")
    @Operation(summary = "오늘의 챌린지 조회 메서드", description = "오늘의 챌린지를 조회 합니다.")
    public List<ChallengeParticipantDTO> getTodayChallengeContents(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // "Bearer "를 제외한 부분

        return challengeParticipantService.getChallengeContentsForToday(userId);
    }


}
