package com.example.beginnerfitbe.challengeparticipant.controller;

import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeDateDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeParticipantDTO;
import com.example.beginnerfitbe.challengeparticipant.dto.ChallengeRankingDto;
import com.example.beginnerfitbe.challengeparticipant.service.ChallengeParticipantService;
import com.example.beginnerfitbe.jwt.util.JwtUtil;
import com.example.beginnerfitbe.user.dto.OtherUserDto;
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
    public long getCompletedFriendCount(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // "Bearer "를 제외한 부분

        // 친구 목록 가져오기
        List<OtherUserDto> acceptedRequestsDtos = challengeParticipantService.getAcceptedFriends(userId);
        List<Long> friendIds = acceptedRequestsDtos.stream()
                .map(OtherUserDto::getId)
                .collect(Collectors.toList());

        // 친구 중 오늘 완료한 친구 수 카운트
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

    @PutMapping("/complete/{challengeId}") // 챌린지 완료 처리
    public ResponseEntity<Void> completeChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.completeChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @PutMapping("/notcomplete/{challengeId}") // 챌린지 실패 처리
    public ResponseEntity<Void> notcompleteChallenge(HttpServletRequest request, @PathVariable Long challengeId ) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        challengeParticipantService.notcompleteChallenge(userId, challengeId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

    @PostMapping("/completed-month-challenges") // 특정 년도, 월의 완료된 챌린지 조회
    public ResponseEntity<Map<String, Object>> getCompletedChallenges(HttpServletRequest request, @RequestBody ChallengeDateDTO challengeDateDTO) {

        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeParticipantDTO> completedChallenges = challengeParticipantService.getCompletedChallengesByDate(
                userId, challengeDateDTO.getYear(), challengeDateDTO.getMonth());

        // 결과를 HashMap으로 구성
        Map<String, Object> response = new HashMap<>();
        response.put("count", completedChallenges.size()); // 리스트의 개수
        response.put("challenges", completedChallenges); // 챌린지 목록

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking") // 친구와의 챌린지 완료 수에 따른 랭킹 조회
    public ResponseEntity<List<ChallengeRankingDto>> getChallengeRankings(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7));

        List<ChallengeRankingDto> rankings = challengeParticipantService.getChallengeRankings(userId);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/today")
    public List<ChallengeParticipantDTO> getTodayChallengeContents(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long userId = jwtUtil.getUserId(token.substring(7)); // "Bearer "를 제외한 부분

        return challengeParticipantService.getChallengeContentsForToday(userId);
    }


}
